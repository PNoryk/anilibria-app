package ru.radiationx.anilibria.presentation.auth.vk

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.radiationx.anilibria.ui.fragments.auth.vk.AuthVkScreenState
import tv.anilibria.core.types.AbsoluteUrl

@StateStrategyType(AddToEndSingleStrategy::class)
interface AuthVkView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun loadPage(url: AbsoluteUrl, resultPattern: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showState(state: AuthVkScreenState)
}