package ru.radiationx.anilibria

import toothpick.InjectConstructor
import tv.anilibria.feature.appupdates.data.CheckerReserveSources

@InjectConstructor
class MobileCheckerSources : CheckerReserveSources {

    override val sources: List<String> = listOf(
        "https://github.com/anilibria/anilibria-app/raw/master/check.json",
        "https://bitbucket.org/RadiationX/anilibria-app/raw/master/check.json"
    )
}