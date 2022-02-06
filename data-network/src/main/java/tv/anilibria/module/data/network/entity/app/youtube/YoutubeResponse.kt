package tv.anilibria.module.data.network.entity.app.youtube

data class YoutubeResponse(
    val id: Int,
    val title: String?,
    val image: String?,
    val vid: String?,
    val views: Int,
    val comments: Int,
    val timestamp: Int
) 