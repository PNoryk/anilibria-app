package ru.radiationx.anilibria.screen.player

import kotlinx.coroutines.flow.MutableSharedFlow
import toothpick.InjectConstructor
import tv.anilibria.feature.domain.entity.release.EpisodeId

@InjectConstructor
class PlayerController {

    val selectEpisodeRelay = MutableSharedFlow<EpisodeId>()
}