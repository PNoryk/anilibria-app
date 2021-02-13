package ru.radiationx.data.interactors

import android.util.Log
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import ru.radiationx.data.SchedulersProvider
import ru.radiationx.data.datasource.remote.address.ApiAddress
import ru.radiationx.data.datasource.remote.address.ApiConfig
import ru.radiationx.data.datasource.remote.address.ApiProxy
import ru.radiationx.data.entity.common.ConfigScreenState
import ru.radiationx.data.repository.ConfigurationRepository
import ru.radiationx.data.system.WrongHostException
import ru.radiationx.shared.ktx.addTo
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.TimeoutException
import javax.inject.Inject
import javax.net.ssl.*

class ConfiguringInteractor @Inject constructor(
    private val apiConfig: ApiConfig,
    private val configurationRepository: ConfigurationRepository,
    private val schedulers: SchedulersProvider
) {

    private val subject = BehaviorSubject.create<ConfigScreenState>()

    private var currentState = State.CHECK_LAST

    private val screenState = ConfigScreenState()

    private val compositeDisposable = CompositeDisposable()

    fun observeScreenState(): Observable<ConfigScreenState> = subject.hide()

    fun initCheck() {
        currentState = State.CHECK_LAST
        updateState(currentState)
        doByState()
    }

    fun repeatCheck() {
        doByState()
    }

    fun nextCheck() {
        val nextState = getNextState() ?: State.CHECK_LAST
        doByState(nextState)
    }

    fun skipCheck() {
        apiConfig.updateNeedConfig(false)
    }

    fun finishCheck() {
        compositeDisposable.clear()
    }

    private fun notifyScreenChanged() {
        subject.onNext(screenState)
    }

    private fun updateState(newState: State) {
        currentState = newState
        screenState.hasNext = getNextState() != null
        notifyScreenChanged()
    }

    private fun doByState(anchor: State = currentState) = when (anchor) {
        State.CHECK_LAST -> checkLast()
        State.LOAD_CONFIG -> loadConfig()
        State.CHECK_AVAIL -> checkAvail()
        State.CHECK_PROXIES -> checkProxies()
    }

    private fun getNextState(anchor: State = currentState): State? = when (anchor) {
        State.CHECK_LAST -> State.LOAD_CONFIG
        State.LOAD_CONFIG -> State.CHECK_AVAIL
        State.CHECK_AVAIL -> State.CHECK_PROXIES
        State.CHECK_PROXIES -> null
    }

    private fun getTitleByState(state: State?): String? = when (state) {
        State.CHECK_LAST -> "Проверка текущих адресов"
        State.LOAD_CONFIG -> "Загрузка новых адресов"
        State.CHECK_AVAIL -> "Проверка новых адресов"
        State.CHECK_PROXIES -> "Проверка прокси-серверов"
        else -> null
    }

    private fun checkLast() {
        updateState(State.CHECK_LAST)
        Log.e("bobobo", "active address ${apiConfig.active}")
        Log.e("bobobo", "getAddresses ${apiConfig.getAddresses()}")
        compositeDisposable.clear()
        zipLastCheck()
            .observeOn(schedulers.ui())
            .doOnSubscribe {
                screenState.status = "Проверка доступности сервера"
                screenState.needRefresh = false
                notifyScreenChanged()
            }
            .subscribe({
                if (it) {
                    screenState.status = "Сервер доступен"
                    notifyScreenChanged()
                    apiConfig.updateNeedConfig(false)
                } else {
                    loadConfig()
                }
            }, {
                Log.e("bobobo", "error on $currentState: $it, ${it is IOException}")
                it.printStackTrace()
                when (it) {
                    is WrongHostException,
                    is TimeoutException -> loadConfig()
                    is IOException,
                    is SSLException,
                    is SSLHandshakeException,
                    is SSLKeyException,
                    is SSLProtocolException,
                    is SSLPeerUnverifiedException -> loadConfig()
                    else -> {
                        screenState.status =
                            "Ошибка проверки доступности сервера: ${it.message}".also {
                                Log.e(
                                    "bobobo",
                                    it
                                )
                            }
                        screenState.needRefresh = true
                        notifyScreenChanged()
                    }
                }
            })
            .addTo(compositeDisposable)
    }

    private fun loadConfig() {
        updateState(State.LOAD_CONFIG)
        compositeDisposable.clear()
        configurationRepository
            .getConfiguration()
            .observeOn(schedulers.ui())
            .doOnSubscribe {
                screenState.status = "Загрузка списка адресов"
                screenState.needRefresh = false
                notifyScreenChanged()
            }
            .subscribe({
                val addresses = apiConfig.getAddresses()
                val proxies = addresses.sumBy { it.proxies.size }
                screenState.status = "Загружено адресов: ${addresses.size}; прокси: $proxies".also {
                    Log.e(
                        "bobobo",
                        it
                    )
                }
                notifyScreenChanged()
                checkAvail()
            }, {
                Log.e("bobobo", "error on $currentState: $it")
                it.printStackTrace()
                screenState.status =
                    "Ошибка загрузки списка адресов: ${it.message}".also { Log.e("bobobo", it) }
                screenState.needRefresh = true
                notifyScreenChanged()
            })
            .addTo(compositeDisposable)
    }

    private fun checkAvail() {
        updateState(State.CHECK_AVAIL)
        compositeDisposable.clear()
        val addresses = apiConfig.getAddresses()
        mergeAvailCheck(addresses)
            .observeOn(schedulers.ui())
            .doOnSubscribe {
                screenState.status = "Проверка доступных адресов"
                screenState.needRefresh = false
                notifyScreenChanged()
            }
            .subscribe({ activeAddress ->
                screenState.status = "Найдет доступный адрес".also { Log.e("bobobo", it) }
                notifyScreenChanged()
                Log.e("boboob", "checkAvail $activeAddress")
                apiConfig.updateActiveAddress(activeAddress)
                apiConfig.updateNeedConfig(false)
            }, {
                Log.e("bobobo", "error on $currentState: $it")
                it.printStackTrace()
                when (it) {
                    // from mergeAvailCheck
                    is NoSuchElementException -> {
                        checkProxies()
                    }
                    else -> {
                        screenState.status =
                            "Ошибка проверки доступности адресов: ${it.message}"
                                .also { Log.e("bobobo", it) }
                        screenState.needRefresh = true
                        notifyScreenChanged()
                    }
                }
            })
            .addTo(compositeDisposable)
    }

    private fun checkProxies() {
        updateState(State.CHECK_PROXIES)
        compositeDisposable.clear()
        val proxies =
            apiConfig.getAddresses().map { it.proxies }.reduce { acc, list -> acc.plus(list) }
        Observable
            .fromArray(proxies)
            .doOnNext {
                if (it.isEmpty()) {
                    throw Exception("No proxies for adresses")
                }
            }
            .flatMap { Observable.fromIterable(it) }
            .concatMapSingle { proxy ->
                configurationRepository.getPingHost(proxy.ip).map { Pair(proxy, it) }
            }
            .filter { !it.second.hasError() }
            .toList()
            .observeOn(schedulers.ui())
            .doOnSubscribe {
                screenState.status = "Проверка доступных прокси"
                screenState.needRefresh = false
                notifyScreenChanged()
            }
            .subscribe({
                it.forEach {
                    apiConfig.setProxyPing(it.first, it.second.timeTaken)
                }
                val bestProxy = it.minBy { it.second.timeTaken }
                val addressByProxy =
                    apiConfig.getAddresses().find { it.proxies.contains(bestProxy?.first) }
                if (bestProxy != null && addressByProxy != null) {
                    apiConfig.updateActiveAddress(addressByProxy)
                    screenState.status =
                        "Доступнные прокси: ${it.size}; будет использован ${bestProxy.first.tag} адреса ${addressByProxy.tag}".also {
                            Log.e(
                                "bobobo",
                                it
                            )
                        }
                    notifyScreenChanged()
                    apiConfig.updateNeedConfig(false)
                }

            }, {
                Log.e("bobobo", "error on $currentState: $it")
                it.printStackTrace()
                screenState.status =
                    "Ошибка проверки доступности прокси-серверов: ${it.message}".also {
                        Log.e(
                            "bobobo",
                            it
                        )
                    }
                screenState.needRefresh = true
                notifyScreenChanged()
            })
            .addTo(compositeDisposable)
    }

    private fun mergeAvailCheck(addresses: List<ApiAddress>): Single<ApiAddress> {
        val adressesSources = addresses.map { address ->
            configurationRepository.checkAvailable(address.api)
                .subscribeOn(schedulers.io())
                .onErrorReturnItem(false)
                .map { Pair(address, it) }
        }
        return Single
            .merge(adressesSources)
            .filter { it.second }
            .map { it.first }
            .firstOrError()
    }

    private fun zipLastCheck(): Single<Boolean> = Single.zip(
        configurationRepository
            .checkAvailable(apiConfig.apiUrl)
            .subscribeOn(schedulers.io()),
        configurationRepository
            .checkApiAvailable(apiConfig.apiUrl)
            .subscribeOn(schedulers.io()),
        BiFunction<Boolean, Boolean, Boolean> { result1, result2 ->
            return@BiFunction result1 && result2
        }
    )

    private enum class State {
        CHECK_LAST,
        LOAD_CONFIG,
        CHECK_AVAIL,
        CHECK_PROXIES
    }
}