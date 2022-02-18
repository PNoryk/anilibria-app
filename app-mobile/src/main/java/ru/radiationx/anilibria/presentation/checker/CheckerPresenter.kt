package ru.radiationx.anilibria.presentation.checker

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.radiationx.anilibria.presentation.common.IErrorHandler
import ru.radiationx.anilibria.presentation.common.viewModelScope
import tv.anilibria.feature.appupdates.data.CheckerRepository
import tv.anilibria.module.data.analytics.features.UpdaterAnalytics
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

    private var compositeDisposable = CompositeDisposable()

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

    override fun onDestroy() {
        compositeDisposable.dispose()
    }

    fun Disposable.addToDisposable() {
        compositeDisposable.add(this)
    }
}