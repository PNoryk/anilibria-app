package tv.anilibria.feature.networkconfig.data

data class ConfigScreenState(
    var status: String = "",
    var needRefresh: Boolean = false,
    var hasNext: Boolean = false
)