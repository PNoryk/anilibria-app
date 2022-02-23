package ru.radiationx.anilibria.presentation.release.details

import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.radiationx.anilibria.presentation.common.IBaseView
import ru.radiationx.anilibria.ui.fragments.release.details.ReleasePagerState
import tv.anilibria.module.domain.entity.release.Release

/* Created by radiationx on 18.11.17. */

@StateStrategyType(AddToEndSingleStrategy::class)
interface ReleaseView : IBaseView {

    fun showState(state: ReleasePagerState)
}
