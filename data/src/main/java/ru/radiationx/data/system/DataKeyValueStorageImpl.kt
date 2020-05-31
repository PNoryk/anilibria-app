package ru.radiationx.data.system

import android.content.SharedPreferences
import anilibria.tv.storage.common.KeyValueStorage
import io.reactivex.Completable
import io.reactivex.Maybe
import ru.radiationx.data.DataPreferences
import toothpick.InjectConstructor

@InjectConstructor
class DataKeyValueStorageImpl(
    @DataPreferences private val sharedPreferences: SharedPreferences
) : KeyValueStorage {

    companion object {
        private const val PREFIX = "data."
    }

    override fun getValue(key: String): Maybe<String> = Maybe.fromCallable {
        sharedPreferences.getString("$PREFIX$key", null)
    }

    override fun putValue(key: String, value: String?): Completable = Completable.fromAction {
        sharedPreferences.edit().putString("$PREFIX$key", value).apply()
    }

    override fun delete(key: String): Completable = Completable.fromAction {
        sharedPreferences.edit().remove("$PREFIX$key").apply()
    }
}