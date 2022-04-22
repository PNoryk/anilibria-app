package tv.anilibria.feature.networkconfig.data.di

import android.content.Context
import toothpick.InjectConstructor
import tv.anilibria.plugin.data.storage.DataStorage
import tv.anilibria.plugin.data.storage.PreferencesDataStorageImpl
import javax.inject.Provider

@InjectConstructor
class ConfigDataStorageProvider(private val context: Context) : Provider<DataStorage> {
    override fun get(): DataStorage {
        return PreferencesDataStorageImpl("data.network_config", context)
    }
}