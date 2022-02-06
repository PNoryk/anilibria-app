package tv.anilibria.module.data.network.entity.common

data class ConfigScreenState(
    var status: String = "",
    var needRefresh: Boolean = false,
    var hasNext: Boolean = false
)