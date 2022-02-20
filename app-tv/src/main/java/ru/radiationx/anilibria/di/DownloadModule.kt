package ru.radiationx.anilibria.di

import toothpick.config.Module
import tv.anilibria.feature.downloads.data.shared.DownloadController
import tv.anilibria.feature.downloads.data.shared.DownloadControllerImpl
import tv.anilibria.feature.downloads.data.shared.DownloadsDataSource

class DownloadModule : Module() {

    init {
        bind(DownloadsDataSource::class.java).singleton()
        bind(DownloadControllerImpl::class.java).to(DownloadControllerImpl::class.java).singleton()
        bind(DownloadController::class.java).to(DownloadControllerImpl::class.java).singleton()
    }
}