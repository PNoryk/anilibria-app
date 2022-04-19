package ru.radiationx.anilibria.navigation

import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import toothpick.InjectConstructor

/* Cicerone навигация
* root - для активити
* local - для табов, типа как в семпле cicerone
* */

@InjectConstructor
class NavigationRoot {
    private val cicerone: Cicerone<Router> = Cicerone.create(Router())

    val router: Router = cicerone.router
    val holder: NavigatorHolder = cicerone.navigatorHolder
}