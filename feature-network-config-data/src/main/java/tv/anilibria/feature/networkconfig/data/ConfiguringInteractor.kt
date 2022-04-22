package tv.anilibria.feature.networkconfig.data

import android.util.Log
import kotlinx.coroutines.flow.*
import toothpick.InjectConstructor
import tv.anilibria.feature.networkconfig.data.address.ApiConfigController
import tv.anilibria.feature.networkconfig.data.domain.ApiAddress
import tv.anilibria.plugin.data.analytics.TimeCounter
import java.io.IOException
import java.util.concurrent.TimeoutException
import javax.net.ssl.*

@InjectConstructor
class ConfiguringInteractor(
    private val apiConfig: ApiConfigController,
    private val configurationRepository: ConfigurationRepository,
    private val analytics: ConfiguringAnalytics
) {

    private val subject = MutableStateFlow(ConfigScreenState())

    private var currentState = State.CHECK_LAST

    private val fullTimeCounter = TimeCounter()

    private var isFullSuccess = false
    private var startAddressTag: String? = null

    fun observeScreenState(): Flow<ConfigScreenState> = subject.onEach {
        Log.d("kekeke", "config status ${it.status}")
    }

    // todo error handling in presentation
    suspend fun initCheck() {
        startAddressTag = apiConfig.getTag()
        fullTimeCounter.start()
        currentState = State.CHECK_LAST
        updateState(currentState)
        doByState()
    }

    // todo error handling in presentation
    suspend fun repeatCheck() {
        analytics.onRepeatClick(currentState.toAnalyticsState())
        doByState()
    }

    // todo error handling in presentation
    suspend fun nextCheck() {
        analytics.onNextStepClick(currentState.toAnalyticsState())
        val nextState = getNextState() ?: State.CHECK_LAST
        doByState(nextState)
    }

    // todo error handling in presentation
    suspend fun skipCheck() {
        isFullSuccess = false
        analytics.onSkipClick(currentState.toAnalyticsState())
        apiConfig.updateNeedConfig(false)
    }

    // todo error handling in presentation
    suspend fun finishCheck() {
        fullTimeCounter.pause()
        analytics.checkFull(
            startAddressTag ?: apiConfig.getTag(),
            apiConfig.getTag(),
            fullTimeCounter.elapsed(),
            isFullSuccess
        )
    }

    private fun updateState(newState: State) {
        currentState = newState
        subject.update { it.copy(hasNext = getNextState() != null) }
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

        subject.update {
            it.copy(
                status = "Проверка доступности сервера",
                needRefresh = false
            )
        }

        runCatching {
            zipLastCheck()
        }.onSuccess {
            timeCounter.pause()
            analytics.checkLast(apiConfig.getTag(), timeCounter.elapsed(), it, null)
            if (it) {
                isFullSuccess = true
                subject.update { it.copy(status = "Сервер доступен") }
                apiConfig.updateNeedConfig(false)
            } else {
                loadConfig()
            }
        }.onFailure { ex ->
            timeCounter.pause()
            Log.e("bobobo", "error on $currentState: $ex, ${ex is IOException}")
            analytics.checkLast(apiConfig.getTag(), timeCounter.elapsed(), false, ex)
            ex.printStackTrace()
            when (ex) {
                is TimeoutException -> loadConfig()
                is IOException,
                is SSLException,
                is SSLHandshakeException,
                is SSLKeyException,
                is SSLProtocolException,
                is SSLPeerUnverifiedException -> loadConfig()
                else -> {
                    subject.update {
                        it.copy(
                            needRefresh = true,
                            status = "Ошибка проверки доступности сервера: ${ex.message}"
                        )
                    }
                }
            }
        }
    }

    private suspend fun loadConfig() {
        updateState(State.LOAD_CONFIG)
        val timeCounter = TimeCounter()


        timeCounter.start()
        subject.update {
            it.copy(
                status = "Загрузка списка адресов",
                needRefresh = false
            )
        }

        runCatching {
            configurationRepository.getConfiguration()
        }.onSuccess {
            timeCounter.pause()
            analytics.loadConfig(timeCounter.elapsed(), true, null)
            val addresses = apiConfig.getAddresses()
            val proxies = addresses.sumOf { it.proxies.size }
            subject.update {
                it.copy(status = "Загружено адресов: ${addresses.size}; прокси: $proxies")
            }
            checkAvail()
        }.onFailure { ex ->
            timeCounter.pause()
            Log.e("bobobo", "error on $currentState: $ex")
            analytics.loadConfig(timeCounter.elapsed(), false, ex)
            ex.printStackTrace()

            subject.update {
                it.copy(
                    status = "Ошибка загрузки списка адресов: ${ex.message}",
                    needRefresh = true
                )
            }
        }
    }

    private suspend fun checkAvail() {
        updateState(State.CHECK_AVAIL)
        val timeCounter = TimeCounter()
        val addresses = apiConfig.getAddresses()


        timeCounter.start()
        subject.update {
            it.copy(
                status = "Проверка доступных адресов",
                needRefresh = false
            )
        }

        runCatching {
            mergeAvailCheck(addresses)
        }.onSuccess { activeAddress ->
            timeCounter.pause()
            isFullSuccess = true
            analytics.checkAvail(activeAddress.tag, timeCounter.elapsed(), true, null)
            subject.update {
                it.copy(status = "Найдет доступный адрес")
            }
            Log.e("boboob", "checkAvail $activeAddress")
            apiConfig.updateActiveAddress(activeAddress)
            apiConfig.updateNeedConfig(false)
        }.onFailure { ex ->
            timeCounter.pause()
            Log.e("bobobo", "error on $currentState: $ex")
            analytics.checkAvail(null, timeCounter.elapsed(), false, ex)
            ex.printStackTrace()
            when (ex) {
                // from mergeAvailCheck
                is NoSuchElementException -> {
                    checkProxies()
                }
                else -> {

                    subject.update {
                        it.copy(
                            status = "Ошибка проверки доступности адресов: ${ex.message}",
                            needRefresh = true
                        )
                    }
                }
            }
        }
    }

    private suspend fun checkProxies() {
        updateState(State.CHECK_PROXIES)
        val timeCounter = TimeCounter()

        timeCounter.start()
        subject.update {
            it.copy(
                status = "Проверка доступных прокси",
                needRefresh = false
            )
        }

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
        }.onSuccess { proxies ->
            timeCounter.pause()
            val bestProxy = proxies.minByOrNull { it.second.timeTaken }
            val addressByProxy =
                apiConfig.getAddresses().find { it.proxies.contains(bestProxy?.first) }
            analytics.checkProxies(addressByProxy?.tag, timeCounter.elapsed(), true, null)
            if (bestProxy != null && addressByProxy != null) {
                isFullSuccess = true
                apiConfig.updateActiveAddress(addressByProxy)
                subject.update {
                    it.copy(
                        status = "Доступнные прокси: ${proxies.size}; будет использован ${bestProxy.first.tag} адреса ${addressByProxy.tag}"
                    )
                }
                apiConfig.updateNeedConfig(false)
            } else {
                subject.update {
                    it.copy(
                        status = "Не найдены доступные прокси",
                        needRefresh = true
                    )
                }
            }
        }.onFailure { ex ->
            timeCounter.pause()
            Log.e("bobobo", "error on $currentState: $ex")
            analytics.checkProxies(null, timeCounter.elapsed(), false, ex)
            ex.printStackTrace()
            subject.update {
                it.copy(
                    status = "Ошибка проверки доступности прокси-серверов: ${ex.message}",
                    needRefresh = true
                )
            }
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