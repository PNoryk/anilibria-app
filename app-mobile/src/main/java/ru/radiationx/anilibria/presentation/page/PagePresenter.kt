package ru.radiationx.anilibria.presentation.page

import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.radiationx.anilibria.presentation.common.BasePresenter
import ru.radiationx.anilibria.presentation.common.IErrorHandler
import ru.terrakok.cicerone.Router
import tv.anilibria.module.data.analytics.features.PageAnalytics
import tv.anilibria.module.data.repos.PageRepository
import javax.inject.Inject

/**
 * Created by radiationx on 13.01.18.
 */
@InjectViewState
class PagePresenter @Inject constructor(
    private val pageRepository: PageRepository,
    private val router: Router,
    private val errorHandler: IErrorHandler,
    private val pageAnalytics: PageAnalytics
) : BasePresenter<PageView>(router) {

    var pagePath: String? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        pagePath?.also {
            loadPage(it)
        }
    }

    private fun loadPage(pagePath: String) {
        viewState.setRefreshing(true)
        viewModelScope.launch {
            runCatching {
                pageRepository.getPage(pagePath)
            }.onSuccess { page ->
                viewState.setRefreshing(false)
                viewState.showPage(page)
            }.onFailure {
                viewState.setRefreshing(false)
                pageAnalytics.error(it)
                errorHandler.handle(it)
            }
        }
    }
}