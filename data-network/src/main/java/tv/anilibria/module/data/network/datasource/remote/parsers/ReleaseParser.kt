package tv.anilibria.module.data.network.datasource.remote.parsers

import org.json.JSONArray
import org.json.JSONObject
import ru.radiationx.shared.ktx.android.mapObjects
import ru.radiationx.shared.ktx.android.mapStrings
import ru.radiationx.shared.ktx.android.nullGet
import ru.radiationx.shared.ktx.android.nullString
import tv.anilibria.module.data.network.datasource.remote.IApiUtils
import tv.anilibria.module.data.network.datasource.remote.address.ApiConfig
import tv.anilibria.module.data.network.entity.app.PaginatedResponse
import tv.anilibria.module.data.network.entity.app.release.*
import javax.inject.Inject

/**
 * Created by radiationx on 18.12.17.
 */
class ReleaseParser @Inject constructor(
    private val apiUtils: IApiUtils,
    private val apiConfig: ApiConfig
) {

    fun parseRandomRelease(jsonItem: JSONObject): RandomReleaseResponse = RandomReleaseResponse(
        jsonItem.getString("code")
    )

    fun parseRelease(jsonResponse: JSONObject): ReleaseResponse {
        return release(jsonResponse)
    }

    fun releases(jsonItems: JSONArray): List<ReleaseResponse> {
        val resItems = mutableListOf<ReleaseResponse>()
        for (i in 0 until jsonItems.length()) {
            val jsonItem = jsonItems.getJSONObject(i)
            resItems.add(parseRelease(jsonItem))
        }
        return resItems
    }

    fun releases(jsonResponse: JSONObject): PaginatedResponse<List<ReleaseResponse>> {
        val jsonItems = jsonResponse.getJSONArray("items")
        val resItems = releases(jsonItems)
        val pagination = PaginatedResponse(resItems)
        val jsonNav = jsonResponse.getJSONObject("pagination")
        jsonNav.nullGet("page")?.let { pagination.page = it.toString().toInt() }
        jsonNav.nullGet("perPage")?.let { pagination.perPage = it.toString().toInt() }
        jsonNav.nullGet("allPages")?.let { pagination.allPages = it.toString().toInt() }
        jsonNav.nullGet("allItems")?.let { pagination.allItems = it.toString().toInt() }
        return pagination
    }

    fun release(jsonResponse: JSONObject): ReleaseResponse {

        val names = jsonResponse.getJSONArray("names")?.mapStrings { name ->
            apiUtils.escapeHtml(name).toString()
        }

        val favoriteInfo = jsonResponse.optJSONObject("favorite")?.let { jsonFavorite ->
            FavoriteInfoResponse(
                jsonFavorite.getInt("rating"),
                jsonFavorite.getBoolean("added")
            )
        }

        val blockedInfo = jsonResponse.optJSONObject("blockedInfo")?.let { jsonBlockedInfo ->
            BlockedInfoResponse(
                jsonBlockedInfo.getBoolean("blocked"),
                jsonBlockedInfo.nullString("reason")
            )
        }

        val onlineEpisodes = jsonResponse
            .optJSONArray("playlist")
            ?.mapObjects { jsonEpisode ->
                EpisodeResponse(
                    jsonEpisode.optInt("id"),
                    jsonEpisode.nullString("title"),
                    jsonEpisode.nullString("sd"),
                    jsonEpisode.nullString("hd"),
                    jsonEpisode.nullString("fullhd"),
                    jsonEpisode.nullString("srcSd"),
                    jsonEpisode.nullString("srcHd"),
                    jsonEpisode.nullString("srcFullHd")
                )
            }

        val externalPlaylists = jsonResponse
            .optJSONArray("externalPlaylist")
            ?.mapObjects { jsonPlaylist ->
                val episodes = jsonPlaylist.getJSONArray("episodes").mapObjects { jsonEpisode ->
                    ExternalEpisodeResponse(
                        id = jsonEpisode.getInt("id"),
                        title = jsonEpisode.nullString("title"),
                        url = jsonEpisode.nullString("url")
                    )
                }

                ExternalPlaylistResponse(
                    tag = jsonPlaylist.getString("tag"),
                    title = jsonPlaylist.getString("title"),
                    actionText = jsonPlaylist.getString("actionText"),
                    episodes = episodes
                )
            }

        val torrents = jsonResponse.getJSONArray("torrents")?.mapObjects { jsonTorrent ->
            TorrentResponse(
                jsonTorrent.optInt("id"),
                jsonTorrent.nullString("hash"),
                jsonTorrent.optInt("leechers"),
                jsonTorrent.optInt("seeders"),
                jsonTorrent.optInt("completed"),
                jsonTorrent.nullString("quality"),
                jsonTorrent.nullString("series"),
                jsonTorrent.optLong("size"),
                "${apiConfig.baseImagesUrl}${jsonTorrent.nullString("url")}"
            )
        }


        val release = ReleaseResponse(
            jsonResponse.getInt("id"),
            jsonResponse.nullString("code"),
            names,
            jsonResponse.nullString("series"),
            "${apiConfig.baseImagesUrl}${jsonResponse.nullString("poster")}",
            jsonResponse.nullString("last")?.toIntOrNull(),
            jsonResponse.nullString("status"),
            jsonResponse.nullString("statusCode"),
            jsonResponse.nullString("type"),
            jsonResponse.optJSONArray("genres")?.mapStrings { it },
            jsonResponse.optJSONArray("voices")?.mapStrings { it },
            jsonResponse.nullString("year"),
            jsonResponse.nullString("season"),
            jsonResponse.nullString("day"),
            jsonResponse.nullString("description")?.trim(),
            jsonResponse.nullString("announce")?.trim(),
            favoriteInfo,
            jsonResponse.optBoolean("showDonateDialog"),
            blockedInfo,
            jsonResponse.nullString("moon"),
            onlineEpisodes,
            externalPlaylists,
            torrents
        )
        return release
    }

}
