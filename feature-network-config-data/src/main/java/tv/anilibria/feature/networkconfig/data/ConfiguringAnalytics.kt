package tv.anilibria.feature.networkconfig.data

import toothpick.InjectConstructor
import tv.anilibria.plugin.data.analytics.*

@InjectConstructor
class ConfiguringAnalytics(
    private val sender: AnalyticsSender
) {

    private companion object {
        const val PARAM_ADDRESS = "address"
        const val PARAM_START_ADDRESS = "start_address"
        const val PARAM_END_ADDRESS = "start_address"
    }

    fun open() {
        sender.send(AnalyticsConstants.config_open)
    }

    fun checkFull(
        startAddressTag: String,
        endAddressTag: String,
        timeInMillis: Long,
        isSuccess: Boolean
    ) {
        sender.send(
            AnalyticsConstants.config_check_full,
            startAddressTag.toParam(PARAM_START_ADDRESS),
            endAddressTag.toParam(PARAM_END_ADDRESS),
            timeInMillis.toTimeParam(),
            isSuccess.toSuccessParam()
        )
    }


    fun checkLast(
        addressTag: String,
        timeInMillis: Long,
        isSuccess: Boolean,
        error: Throwable? = null
    ) {
        sender.send(
            AnalyticsConstants.config_check_last,
            addressTag.toParam(PARAM_ADDRESS),
            timeInMillis.toTimeParam(),
            isSuccess.toSuccessParam(),
            error.toErrorParam()
        )
    }

    fun loadConfig(
        timeInMillis: Long,
        isSuccess: Boolean,
        error: Throwable? = null
    ) {
        sender.send(
            AnalyticsConstants.config_load_config,
            timeInMillis.toTimeParam(),
            isSuccess.toSuccessParam(),
            error.toErrorParam()
        )
    }

    fun checkAvail(
        addressTag: String?,
        timeInMillis: Long,
        isSuccess: Boolean,
        error: Throwable? = null
    ) {
        sender.send(
            AnalyticsConstants.config_check_avail,
            addressTag.toParam(PARAM_ADDRESS),
            timeInMillis.toTimeParam(),
            isSuccess.toSuccessParam(),
            error.toErrorParam()
        )
    }

    fun checkProxies(
        addressTag: String?,
        timeInMillis: Long,
        isSuccess: Boolean,
        error: Throwable? = null
    ) {
        sender.send(
            AnalyticsConstants.config_check_proxies,
            addressTag.toParam(PARAM_ADDRESS),
            timeInMillis.toTimeParam(),
            isSuccess.toSuccessParam(),
            error.toErrorParam()
        )
    }

    fun onRepeatClick(state: AnalyticsConfigState) {
        sender.send(
            AnalyticsConstants.config_repeat,
            state.toStateParam()
        )
    }

    fun onSkipClick(state: AnalyticsConfigState) {
        sender.send(
            AnalyticsConstants.config_skip,
            state.toStateParam()
        )
    }

    fun onNextStepClick(state: AnalyticsConfigState) {
        sender.send(
            AnalyticsConstants.config_next,
            state.toStateParam()
        )
    }


    private fun AnalyticsConfigState?.toStateParam(name: String = "state") =
        Pair(name, this.toString())
}