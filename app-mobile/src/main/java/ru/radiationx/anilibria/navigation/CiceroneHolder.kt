package ru.radiationx.anilibria.navigation

import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router
import toothpick.InjectConstructor
import javax.inject.Inject

@InjectConstructor
class CiceroneHolder {
    private val containers: MutableMap<String, Cicerone<Router>> = mutableMapOf()

    fun getCicerone(containerTag: String): Cicerone<Router> {
        if (!containers.containsKey(containerTag)) {
            containers[containerTag] = Cicerone.create(Router())
        }
        return containers.getValue(containerTag)
    }
}