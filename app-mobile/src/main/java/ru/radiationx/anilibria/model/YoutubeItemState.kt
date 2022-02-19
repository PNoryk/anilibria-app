package ru.radiationx.anilibria.model

import tv.anilibria.module.domain.entity.youtube.YoutubeId

data class YoutubeItemState(
    val id: YoutubeId,
    val title: String,
    val image: String,
    val views: String,
    val comments: String
)