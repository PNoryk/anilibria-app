package tv.anilibria.feature.networkconfig.data

data class ConfigScreenState(
    val status: String = "",
    val needRefresh: Boolean = false,
    val hasNext: Boolean = false
)