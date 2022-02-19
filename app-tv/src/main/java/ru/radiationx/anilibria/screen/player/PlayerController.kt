package ru.radiationx.anilibria.screen.player

import com.jakewharton.rxrelay2.PublishRelay
import toothpick.InjectConstructor
import tv.anilibria.module.domain.entity.release.EpisodeId

@InjectConstructor
class PlayerController {

    val selectEpisodeRelay = PublishRelay.create<EpisodeId>()
}