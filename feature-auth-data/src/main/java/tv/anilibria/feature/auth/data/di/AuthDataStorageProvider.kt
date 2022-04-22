package tv.anilibria.feature.auth.data.di

import android.content.Context
import toothpick.InjectConstructor
import tv.anilibria.plugin.data.storage.DataStorage
import tv.anilibria.plugin.data.storage.PreferencesDataStorageImpl
import javax.inject.Provider

@InjectConstructor
class AuthDataStorageProvider(private val context: Context) : Provider<DataStorage> {
    override fun get(): DataStorage {
        return PreferencesDataStorageImpl("data.auth", context)
    }
}