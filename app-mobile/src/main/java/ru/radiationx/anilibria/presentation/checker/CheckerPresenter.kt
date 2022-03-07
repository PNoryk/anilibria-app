package ru.radiationx.anilibria.presentation.checker

import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.radiationx.anilibria.presentation.common.IErrorHandler
import ru.radiationx.anilibria.presentation.common.viewModelScope
import tv.anilibria.feature.appupdates.data.CheckerRepository
import tv.anilibria.feature.data.analytics.features.UpdaterAnalytics
import tv.anilibria.plugin.shared.appinfo.SharedBuildConfig
import javax.inject.Inject

/**
 * Created by radiationx on 28.01.18.
 */
@InjectViewState
class CheckerPresenter @Inject constructor(
    private val checkerRepository: CheckerRepository,
    private val errorHandler: IErrorHandler,
    private val updaterAnalytics: UpdaterAnalytics,
    private val sharedBuildConfig: SharedBuildConfig
) : MvpPresenter<CheckerView>() {

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