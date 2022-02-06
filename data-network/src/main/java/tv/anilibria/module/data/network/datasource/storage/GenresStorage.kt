package tv.anilibria.module.data.network.datasource.storage

import android.content.SharedPreferences
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import org.json.JSONArray
import org.json.JSONObject
import tv.anilibria.module.data.network.DataPreferences
import tv.anilibria.module.data.network.datasource.holders.GenresHolder
import tv.anilibria.module.data.network.entity.app.release.GenreItem
import javax.inject.Inject

/**
 * Created by radiationx on 17.02.18.
 */
class GenresStorage @Inject constructor(
        @DataPreferences private val sharedPreferences: SharedPreferences
) : GenresHolder {

    companion object {
        private const val LOCAL_GENRES_KEY = "data.local_genres"
    }

    private val localGenres = mutableListOf<GenreItem>()
    private val localGenresRelay = BehaviorRelay.createDefault(localGenres)

    init {
        loadAll()
    }

    override fun observeGenres(): Observable<MutableList<GenreItem>> = localGenresRelay

    override fun saveGenres(genres: List<GenreItem>) {
        localGenres.clear()
        localGenres.addAll(genres)
        saveAll()
        localGenresRelay.accept(localGenres)
    }

    override fun getGenres(): List<GenreItem> = localGenres

    private fun saveAll() {
        val jsonGenres = JSONArray()
        localGenres.forEach {
            jsonGenres.put(JSONObject().apply {
                put("title", it.title)
                put("value", it.value)
            })
        }
        sharedPreferences
                .edit()
                .putString(LOCAL_GENRES_KEY, jsonGenres.toString())
                .apply()
    }

    private fun loadAll() {
        val savedGenres = sharedPreferences.getString(LOCAL_GENRES_KEY, null)
        savedGenres?.let {
            val jsonGenres = JSONArray(it)
            (0 until jsonGenres.length()).forEach {
                jsonGenres.getJSONObject(it).let {
                    localGenres.add(GenreItem().apply {
                        title = it.getString("title")
                        value = it.getString("value")
                    })
                }
            }
        }
        localGenresRelay.accept(localGenres)
    }
}