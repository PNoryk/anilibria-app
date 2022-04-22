package ru.radiationx.anilibria

import toothpick.InjectConstructor
import tv.anilibria.feature.networkconfig.data.ApiConfigReserveSources

@InjectConstructor
class MobileConfigSources : ApiConfigReserveSources {

    override val sources: List<String> = listOf(
        "https://raw.githubusercontent.com/anilibria/anilibria-app/master/config.json",
        "https://bitbucket.org/RadiationX/anilibria-app/raw/master/config.json"
    )
}