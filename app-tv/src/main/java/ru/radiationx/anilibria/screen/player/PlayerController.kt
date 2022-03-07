package ru.radiationx.anilibria.screen.player

import kotlinx.coroutines.flow.MutableSharedFlow
import toothpick.InjectConstructor
import tv.anilibria.feature.content.types.release.EpisodeId

@InjectConstructor
class PlayerController {

    val selectEpisodeRelay = MutableSharedFlow<EpisodeId>()
}