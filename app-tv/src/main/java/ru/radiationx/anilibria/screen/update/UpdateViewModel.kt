package ru.radiationx.anilibria.screen.update

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.radiationx.anilibria.common.fragment.GuidedRouter
import ru.radiationx.anilibria.screen.LifecycleViewModel
import ru.radiationx.anilibria.screen.UpdateSourceScreen
import ru.radiationx.shared_app.common.MimeTypeUtil
import toothpick.InjectConstructor
import tv.anilibria.feature.appupdates.data.CheckerRepository
import tv.anilibria.feature.appupdates.data.domain.UpdateData
import tv.anilibria.feature.downloads.data.shared.DownloadController
import tv.anilibria.feature.downloads.data.shared.DownloadItem
import tv.anilibria.plugin.shared.appinfo.SharedBuildConfig

@InjectConstructor
class UpdateViewModel(
    private val checkerRepository: CheckerRepository,
    private val buildConfig: SharedBuildConfig,
    private val guidedRouter: GuidedRouter,
    private val downloadController: DownloadController,
    private val updateController: UpdateController,
    private val context: Context
) : LifecycleViewModel() {

    val updateData = MutableLiveData<UpdateData>()
    val progressState = MutableLiveData<Boolean>()
    val downloadProgressShowState = MutableLiveData<Boolean>()
    val downloadProgressData = MutableLiveData<Int>()
    val downloadActionTitle = MutableLiveData<String>()

    private var currentDownload: DownloadItem? = null
    private var downloadState: DownloadController.State? = null
    private var pendingInstall = false

    private var updatesJob: Job? = null
    private var completedJob: Job? = null

    init {
        progressState.value = true
        downloadProgressShowState.value = false
        downloadProgressData.value = 0
    }

    override fun onCreate() {
        super.onCreate()
        updateState()

        viewModelScope.launch {
            runCatching {
                checkerRepository.checkUpdate(buildConfig.versionCode, false)
            }.onSuccess {
                progressState.value = false
                updateData.value = it
                it.links.mapNotNull { downloadController.getDownload(it.url.value) }.firstOrNull()
                    ?.also {
                        startDownload(it.url)
                    }
            }.onFailure {
                progressState.value = false
                it.printStackTrace()
            }
        }


        updateController
            .downloadAction
            .onEach {
                pendingInstall = true
                startDownload(it.url.value)
            }
            .launchIn(viewModelScope)
    }

    fun onActionClick() {
        when (downloadState) {
            DownloadController.State.PENDING,
            DownloadController.State.RUNNING,
            DownloadController.State.PAUSED,
            DownloadController.State.FAILED -> cancelDownloadClick()
            else -> downloadClick()
        }
    }

    private fun downloadClick() {
        val data = updateData.value ?: return
        if (data.links.size > 1) {
            guidedRouter.open(UpdateSourceScreen())
        } else {
            val link = data.links.firstOrNull() ?: return
            viewModelScope.launch {
                updateController.downloadAction.emit(link)
            }
        }
    }

    private fun cancelDownloadClick() {
        val url = currentDownload?.url ?: return
        downloadController.removeDownload(url)
    }

    private fun startDownload(url: String) {
        val downloadItem = downloadController.getDownload(url)
        if (currentDownload != null && currentDownload?.url == downloadItem?.url) {
            return
        }
        Log.e("UpdateViewModel", "startDownload by url $url")
        Log.e("UpdateViewModel", "startDownload item $downloadItem")

        updatesJob?.cancel()
        updatesJob = downloadController
            .observeDownload(url)
            .onEach { handleUpdate(it) }
            .catch { it.printStackTrace() }
            .launchIn(viewModelScope)

        completedJob?.cancel()
        completedJob = downloadController
            .observeCompleted(url)
            .onEach { handleComplete(it) }
            .catch { it.printStackTrace() }
            .launchIn(viewModelScope)

        if (downloadItem == null) {
            downloadController.startDownload(url)
        } else {
            if (downloadItem.state == DownloadController.State.SUCCESSFUL) {
                handleComplete(downloadItem)
            } else {
                handleUpdate(downloadItem)
            }
        }


    }

    private fun handleUpdate(downloadItem: DownloadItem) {
        Log.e("UpdateViewModel", "handleUpdate ${currentDownload?.downloadId}, $downloadItem")
        currentDownload = downloadItem
        downloadProgressData.value = downloadItem.progress
        updateState(downloadItem.state)
    }

    private fun handleComplete(downloadItem: DownloadItem) {
        Log.e("UpdateViewModel", "handleComplete $downloadItem")
        currentDownload = null
        updateState(null)
        startPendingInstall(downloadItem)
        updatesJob?.cancel()
        completedJob?.cancel()
    }

    private fun startPendingInstall(downloadItem: DownloadItem) {
        if (pendingInstall && downloadItem.state == DownloadController.State.SUCCESSFUL) {
            pendingInstall = false
            installAction(downloadItem)
        }
    }

    private fun updateState(state: DownloadController.State? = downloadState) {
        downloadState = state
        downloadProgressShowState.value = when (state) {
            DownloadController.State.PENDING,
            DownloadController.State.RUNNING,
            DownloadController.State.PAUSED -> true
            else -> false
        }
        downloadActionTitle.value = when (state) {
            DownloadController.State.PENDING,
            DownloadController.State.RUNNING,
            DownloadController.State.PAUSED -> "Отмена"
            else -> "Установить"
        }
    }

    private fun installAction(downloadItem: DownloadItem) {
        val data = Uri.parse(downloadItem.localUrl)
        val type = MimeTypeUtil.getType(downloadItem.url)
        Log.e("UpdateViewModel", "installAction $data, $type")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val install = Intent(Intent.ACTION_VIEW)
            install.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            install.setDataAndType(data, type)
            context.startActivity(install)
        } else {
            val install = Intent(Intent.ACTION_VIEW)
            install.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            install.setDataAndType(data, type)
            context.startActivity(install)
        }
    }
}