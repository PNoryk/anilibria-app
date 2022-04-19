package tv.anilibria.app.mobile.preferences.di

import toothpick.config.Module
import tv.anilibria.app.mobile.preferences.PreferencesStorage
import tv.anilibria.plugin.data.storage.DataStorage

class MobileAppPreferencesModule : Module() {

    init {
        bind(DataStorage::class.java)
            .withName(PreferencesStorageQualifier::class.java)
            .toProvider(PreferencesDataStorageProvider::class.java)
            .providesSingleton()

        bind(PreferencesStorage::class.java)
    }
}