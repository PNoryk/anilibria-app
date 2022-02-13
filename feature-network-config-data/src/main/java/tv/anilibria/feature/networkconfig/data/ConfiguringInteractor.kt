package tv.anilibria.feature.networkconfig.data

import android.util.Log
import kotlinx.coroutines.flow.*
import tv.anilibria.feature.networkconfig.data.address.ApiConfigController
import tv.anilibria.feature.networkconfig.data.domain.ApiAddress
import tv.anilibria.plugin.data.analytics.TimeCounter
import java.io.IOException
import java.util.concurrent.TimeoutException
import javax.inject.Inject
import javax.net.ssl.*

class ConfiguringInteractor @Inject constructor(
    private val apiConfig: ApiConfigController,
    private val configurationRepository: ConfigurationRepository,
    private val analytics: ConfiguringAnalytics
) {

    private val subject = MutableStateFlow(ConfigScreenState())

    private var currentState = State.CHECK_LAST

    private val screenState = ConfigScreenState()

    private val fullTimeCounter = TimeCounter()

    private var isFullSuccess = false
    private var startAddressTag: String? = null

    fun observeScreenState(): Flow<ConfigScreenState> = subject

    suspend fun initCheck() {
        startAddressTag = apiConfig.getTag()
        fullTimeCounter.start()
        currentState = State.CHECK_LAST
        updateState(currentState)
        doByState()
    }

    suspend fun repeatCheck() {
        analytics.onRepeatClick(currentState.toAnalyticsState())
        doByState()
    }

    suspend fun nextCheck() {
        analytics.onNextStepClick(currentState.toAnalyticsState())
        val nextState = getNextState() ?: State.CHECK_LAST
        doByState(nextState)
    }

    suspend fun skipCheck() {
        isFullSuccess = false
        analytics.onSkipClick(currentState.toAnalyticsState())
        apiConfig.updateNeedConfig(false)
    }

    suspend fun finishCheck() {
        fullTimeCounter.pause()
        analytics.checkFull(
            startAddressTag ?: apiConfig.getTag(),
            apiConfig.getTag(),
            fullTimeCounter.elapsed(),
            isFullSuccess
        )
    }

    private fun notifyScreenChanged() {
        subject.value = screenState
    }

    private fun updateState(newState: State) {
        currentState = newState
        screenState.hasNext = getNextState() != null
        notifyScreenChanged()
    }

    private suspend fun doByState(anchor: State = currentState) = when (anchor) {
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

    private suspend fun checkLast() {
        updateState(State.CHECK_LAST)
        val timeCounter = TimeCounter()

        timeCounter.start()
        screenState.status = "Проверка доступности сервера"
        screenState.needRefresh = false
        notifyScreenChanged()

        runCatching {
            zipLastCheck()
        }.onSuccess {
            timeCounter.pause()
            analytics.checkLast(apiConfig.getTag(), timeCounter.elapsed(), it, null)
            if (it) {
                isFullSuccess = true
                screenState.status = "Сервер доступен"
                notifyScreenChanged()
                apiConfig.updateNeedConfig(false)
            } else {
                loadConfig()
            }
        }.onFailure {
            timeCounter.pause()
            Log.e("bobobo", "error on $currentState: $it, ${it is IOException}")
            analytics.checkLast(apiConfig.getTag(), timeCounter.elapsed(), false, it)
            it.printStackTrace()
            when (it) {
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
        }
    }

    private suspend fun loadConfig() {
        updateState(State.LOAD_CONFIG)
        val timeCounter = TimeCounter()


        timeCounter.start()
        screenState.status = "Загрузка списка адресов"
        screenState.needRefresh = false
        notifyScreenChanged()

        runCatching {
            configurationRepository.getConfiguration()
        }.onSuccess {
            timeCounter.pause()
            analytics.loadConfig(timeCounter.elapsed(), true, null)
            val addresses = apiConfig.getAddresses()
            val proxies = addresses.sumOf { it.proxies.size }
            screenState.status = "Загружено адресов: ${addresses.size}; прокси: $proxies".also {
                Log.e(
                    "bobobo",
                    it
                )
            }
            notifyScreenChanged()
            checkAvail()
        }.onFailure {
            timeCounter.pause()
            Log.e("bobobo", "error on $currentState: $it")
            analytics.loadConfig(timeCounter.elapsed(), false, it)
            it.printStackTrace()
            screenState.status =
                "Ошибка загрузки списка адресов: ${it.message}".also { Log.e("bobobo", it) }
            screenState.needRefresh = true
            notifyScreenChanged()
        }
    }

    private suspend fun checkAvail() {
        updateState(State.CHECK_AVAIL)
        val timeCounter = TimeCounter()
        val addresses = apiConfig.getAddresses()


        timeCounter.start()
        screenState.status = "Проверка доступных адресов"
        screenState.needRefresh = false
        notifyScreenChanged()

        runCatching {
            mergeAvailCheck(addresses)
        }.onSuccess { activeAddress ->
            timeCounter.pause()
            isFullSuccess = true
            analytics.checkAvail(activeAddress.tag, timeCounter.elapsed(), true, null)
            screenState.status = "Найдет доступный адрес".also { Log.e("bobobo", it) }
            notifyScreenChanged()
            Log.e("boboob", "checkAvail $activeAddress")
            apiConfig.updateActiveAddress(activeAddress)
            apiConfig.updateNeedConfig(false)
        }.onFailure {
            timeCounter.pause()
            Log.e("bobobo", "error on $currentState: $it")
            analytics.checkAvail(null, timeCounter.elapsed(), false, it)
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
        }
    }

    private suspend fun checkProxies() {
        updateState(State.CHECK_PROXIES)
        val timeCounter = TimeCounter()

        timeCounter.start()
        screenState.status = "Проверка доступных прокси"
        screenState.needRefresh = false
        notifyScreenChanged()

        runCatching {
            val proxies = apiConfig.getAddresses()
                .map { it.proxies }
                .reduce { acc, list -> acc.plus(list) }

            if (proxies.isEmpty()) {
                throw Exception("No proxies for adresses")
            }
            proxies.asFlow()
                .map { Pair(it, configurationRepository.getPingHost(it.ip)) }
                .filter { !it.second.hasError() }
                .toList()
        }.onSuccess {
            timeCounter.pause()
            val bestProxy = it.minByOrNull { it.second.timeTaken }
            val addressByProxy =
                apiConfig.getAddresses().find { it.proxies.contains(bestProxy?.first) }
            analytics.checkProxies(addressByProxy?.tag, timeCounter.elapsed(), true, null)
            if (bestProxy != null && addressByProxy != null) {
                isFullSuccess = true
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
            } else {
                screenState.status = "Не найдены доступные прокси"
                screenState.needRefresh = true
                notifyScreenChanged()
            }
        }.onFailure {
            timeCounter.pause()
            Log.e("bobobo", "error on $currentState: $it")
            analytics.checkProxies(null, timeCounter.elapsed(), false, it)
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
        }
    }

    private suspend fun mergeAvailCheck(addresses: List<ApiAddress>): ApiAddress {
        val addressesSources = addresses.map { address ->
            val result = runCatching { configurationRepository.checkAvailable(address.api) }
            Pair(address, result.getOrNull() ?: false)
        }
        return addressesSources
            .filter { it.second }
            .map { it.first }
            .first()
    }

    private suspend fun zipLastCheck(): Boolean {
        val defaultAvailable = configurationRepository.checkAvailable(apiConfig.getActive().api)
        val apiAvailable = configurationRepository.checkApiAvailable(apiConfig.getActive().api)
        return defaultAvailable && apiAvailable
    }

    private fun State.toAnalyticsState(): AnalyticsConfigState = when (this) {
        State.CHECK_LAST -> AnalyticsConfigState.CHECK_LAST
        State.LOAD_CONFIG -> AnalyticsConfigState.LOAD_CONFIG
        State.CHECK_AVAIL -> AnalyticsConfigState.CHECK_AVAIL
        State.CHECK_PROXIES -> AnalyticsConfigState.CHECK_PROXIES
    }

    private enum class State {
        CHECK_LAST,
        LOAD_CONFIG,
        CHECK_AVAIL,
        CHECK_PROXIES
    }
}