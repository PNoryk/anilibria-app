package tv.anilibria.feature.downloads.data.di

import toothpick.config.Module
import tv.anilibria.feature.downloads.data.DownloadsStorage
import tv.anilibria.feature.downloads.data.shared.DownloadController
import tv.anilibria.feature.downloads.data.shared.DownloadControllerImpl
import tv.anilibria.feature.downloads.data.shared.DownloadsDataSource
import tv.anilibria.plugin.data.storage.DataStorage

class DownloadsDataFeatureModule : Module() {

    init {
        bind(DataStorage::class.java)
            .withName(DownloadsStorageQualifier::class.java)
            .toProvider(DownloadsDataStorageProvider::class.java)
            .providesSingleton()

        bind(DownloadsDataSource::class.java)
        bind(DownloadsStorage::class.java)
        bind(DownloadController::class.java).to(DownloadControllerImpl::class.java)
    }
}