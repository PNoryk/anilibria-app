package ru.radiationx.anilibria.presentation.checker

import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.radiationx.anilibria.presentation.common.BasePresenter
import ru.radiationx.anilibria.presentation.common.IErrorHandler
import ru.terrakok.cicerone.Router
import toothpick.InjectConstructor
import tv.anilibria.feature.analytics.api.features.UpdaterAnalytics
import tv.anilibria.feature.appupdates.data.CheckerRepository
import tv.anilibria.plugin.shared.appinfo.SharedBuildConfig

@InjectViewState
@InjectConstructor
class CheckerPresenter(
    private val router: Router,
    private val checkerRepository: CheckerRepository,
    private val errorHandler: IErrorHandler,
    private val updaterAnalytics: UpdaterAnalytics,
    private val sharedBuildConfig: SharedBuildConfig
) : BasePresenter<CheckerView>(router) {

    var forceLoad = false

    fun submitUseTime(time: Long) {
        updaterAnalytics.useTime(time)
    }

    fun checkUpdate() {
        viewModelScope.launch {

            runCatching {
                viewState.setRefreshing(true)
                checkerRepository.checkUpdate(sharedBuildConfig.versionCode, forceLoad)
            }.onSuccess {
                viewState.setRefreshing(false)
                viewState.showUpdateData(it)
            }.onFailure {
                viewState.setRefreshing(false)
                errorHandler.handle(it)
            }
        }
    }

    fun onDownloadClick() {
        updaterAnalytics.downloadClick()
    }

    fun onSourceDownloadClick(title: String) {
        updaterAnalytics.sourceDownload(title)
    }
}