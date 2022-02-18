package ru.radiationx.anilibria.presentation.page

import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.radiationx.anilibria.presentation.common.IBaseView
import tv.anilibria.module.domain.entity.page.PageLibria

/**
 * Created by radiationx on 13.01.18.
 */

@StateStrategyType(AddToEndSingleStrategy::class)
interface PageView : IBaseView {
    fun showPage(page: PageLibria)
}