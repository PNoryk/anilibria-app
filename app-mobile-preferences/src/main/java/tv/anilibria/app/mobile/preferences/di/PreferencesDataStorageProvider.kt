package tv.anilibria.app.mobile.preferences.di

import android.content.Context
import tv.anilibria.plugin.data.storage.DataStorage
import tv.anilibria.plugin.data.storage.PreferencesDataStorageImpl
import javax.inject.Provider

class PreferencesDataStorageProvider(private val context: Context) : Provider<DataStorage> {
    override fun get(): DataStorage {
        return PreferencesDataStorageImpl("data.user", context)
    }
}