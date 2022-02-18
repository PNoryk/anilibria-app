package ru.radiationx.anilibria.screen.update

import kotlinx.coroutines.flow.MutableSharedFlow
import toothpick.InjectConstructor
import tv.anilibria.feature.appupdates.data.domain.UpdateLink

@InjectConstructor
class UpdateController {

    val downloadAction = MutableSharedFlow<UpdateLink>()
}