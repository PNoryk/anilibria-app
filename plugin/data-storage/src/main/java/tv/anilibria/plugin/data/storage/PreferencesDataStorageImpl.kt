package tv.anilibria.plugin.data.storage

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import toothpick.InjectConstructor

@InjectConstructor
class PreferencesDataStorageImpl(
    private val name: String,
    private val context: Context
) : DataStorage {

    private val preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    override fun observeAllUpdates(): Flow<String> = callbackFlow<String> {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            trySend(key)
        }
        preferences.registerOnSharedPreferenceChangeListener(listener)
        awaitClose {
            preferences.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun <T> get(key: StorageKey<T>): T {
        return withContext(Dispatchers.IO) {
            val value = when (key) {
                is StorageKey.BooleanKey -> preferences.getBoolean(key.name, key.default)
                is StorageKey.FloatKey -> preferences.getFloat(key.name, key.default)
                is StorageKey.IntKey -> preferences.getInt(key.name, key.default)
                is StorageKey.LongKey -> preferences.getLong(key.name, key.default)
                is StorageKey.StringKey -> preferences.getString(key.name, key.default)
            }
            value as T
        }
    }

    override suspend fun <T> set(key: StorageKey<T>, value: T) {
        withContext(Dispatchers.IO) {
            val editor = preferences.edit()
            when (key) {
                is StorageKey.BooleanKey -> editor.putBoolean(key.name, value as Boolean)
                is StorageKey.FloatKey -> editor.putFloat(key.name, value as Float)
                is StorageKey.IntKey -> editor.putInt(key.name, value as Int)
                is StorageKey.LongKey -> editor.putLong(key.name, value as Long)
                is StorageKey.StringKey -> editor.putString(key.name, value as String?)
            }
            editor.commit()
        }
    }

    override suspend fun <T> remove(key: StorageKey<T>) {
        withContext(Dispatchers.IO) {
            preferences.edit().remove(key.name).commit()
        }
    }

    override suspend fun clear() {
        withContext(Dispatchers.IO) {
            preferences.edit().clear().commit()
        }
    }
}