package tv.anilibria.module.data.network.datasource.storage

import android.content.SharedPreferences
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.Single
import org.json.JSONArray
import org.json.JSONObject
import tv.anilibria.module.data.network.DataPreferences
import tv.anilibria.module.data.network.datasource.holders.EpisodesCheckerHolder
import tv.anilibria.module.data.network.entity.app.release.ReleaseResponse
import javax.inject.Inject

/**
 * Created by radiationx on 17.02.18.
 */
class EpisodesCheckerStorage @Inject constructor(
    @DataPreferences private val sharedPreferences: SharedPreferences
) : EpisodesCheckerHolder {

    companion object {
        private const val LOCAL_EPISODES_KEY = "data.local_episodes"
    }

    private val localEpisodes = mutableListOf<ReleaseResponse.Episode>()
    private val localEpisodesRelay = BehaviorRelay.createDefault(localEpisodes)

    init {
        loadAll()
    }

    override fun observeEpisodes(): Observable<MutableList<ReleaseResponse.Episode>> =
        localEpisodesRelay

    override fun getEpisodes(): Single<List<ReleaseResponse.Episode>> =
        Single.fromCallable { localEpisodesRelay.value!! }

    override fun putEpisode(episode: ReleaseResponse.Episode) {
        localEpisodes
            .firstOrNull { it.releaseId == episode.releaseId && it.id == episode.id }
            ?.let { localEpisodes.remove(it) }
        localEpisodes.add(episode)
        saveAll()
        localEpisodesRelay.accept(localEpisodes)
    }

    override fun putAllEpisode(episodes: List<ReleaseResponse.Episode>) {
        episodes.forEach { episode ->
            localEpisodes
                .firstOrNull { it.releaseId == episode.releaseId && it.id == episode.id }
                ?.let { localEpisodes.remove(it) }
            localEpisodes.add(episode)
        }
        saveAll()
        localEpisodesRelay.accept(localEpisodes)
    }

    override fun getEpisodes(releaseId: Int): List<ReleaseResponse.Episode> {
        return localEpisodes.filter { it.releaseId == releaseId }
    }

    override fun remove(releaseId: Int) {
        localEpisodes.removeAll { it.releaseId == releaseId }
        saveAll()
        localEpisodesRelay.accept(localEpisodes)
    }

    private fun saveAll() {
        val jsonEpisodes = JSONArray()
        localEpisodes.forEach {
            jsonEpisodes.put(JSONObject().apply {
                put("releaseId", it.releaseId)
                put("id", it.id)
                put("seek", it.seek)
                put("isViewed", it.isViewed)
                put("lastAccess", it.lastAccess)
            })
        }
        sharedPreferences
            .edit()
            .putString(LOCAL_EPISODES_KEY, jsonEpisodes.toString())
            .apply()
    }

    private fun loadAll() {
        val savedEpisodes = sharedPreferences.getString(LOCAL_EPISODES_KEY, null)
        savedEpisodes?.let {
            val jsonEpisodes = JSONArray(it)
            (0 until jsonEpisodes.length()).forEach {
                jsonEpisodes.getJSONObject(it).let {
                    localEpisodes.add(ReleaseResponse.Episode().apply {
                        releaseId = it.getInt("releaseId")
                        id = it.getInt("id")
                        seek = it.optLong("seek", 0L)
                        isViewed = it.optBoolean("isViewed", false)
                        lastAccess = it.optLong("lastAccess", 0L)
                    })
                }
            }
        }
        localEpisodesRelay.accept(localEpisodes)
    }
}