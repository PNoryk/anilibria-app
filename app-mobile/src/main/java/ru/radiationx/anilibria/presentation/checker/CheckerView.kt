package ru.radiationx.anilibria.presentation.checker

import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.radiationx.anilibria.presentation.common.IBaseView
import tv.anilibria.feature.appupdates.data.domain.UpdateData

/**
 * Created by radiationx on 28.01.18.
 */

@StateStrategyType(AddToEndSingleStrategy::class)
interface CheckerView : IBaseView {

    fun showUpdateData(update: UpdateData)
}