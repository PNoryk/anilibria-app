package tv.anilibria.module.data.network.datasource.remote.parsers

import org.json.JSONArray
import org.json.JSONObject
import ru.radiationx.shared.ktx.android.mapObjects
import ru.radiationx.shared.ktx.android.mapStrings
import ru.radiationx.shared.ktx.android.nullGet
import ru.radiationx.shared.ktx.android.nullString
import tv.anilibria.module.data.network.entity.app.PaginatedResponse
import tv.anilibria.module.data.network.entity.app.release.*
import javax.inject.Inject

/**
 * Created by radiationx on 18.12.17.
 */
class ReleaseParser @Inject constructor() {

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

        val favoriteInfo = jsonResponse.optJSONObject("favorite")?.let { jsonFavorite ->
            FavoriteInfoResponse(
                rating = jsonFavorite.getInt("rating"),
                isAdded = jsonFavorite.getBoolean("added")
            )
        }

        val blockedInfo = jsonResponse.optJSONObject("blockedInfo")?.let { jsonBlockedInfo ->
            BlockedInfoResponse(
                isBlocked = jsonBlockedInfo.getBoolean("blocked"),
                reason = jsonBlockedInfo.nullString("reason")
            )
        }

        val onlineEpisodes = jsonResponse
            .optJSONArray("playlist")
            ?.mapObjects { jsonEpisode ->
                EpisodeResponse(
                    id = jsonEpisode.optInt("id"),
                    title = jsonEpisode.nullString("title"),
                    urlSd = jsonEpisode.nullString("sd"),
                    urlHd = jsonEpisode.nullString("hd"),
                    urlFullHd = jsonEpisode.nullString("fullhd"),
                    srcUrlSd = jsonEpisode.nullString("srcSd"),
                    srcUrlHd = jsonEpisode.nullString("srcHd"),
                    srcUrlFullHd = jsonEpisode.nullString("srcFullHd")
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
                id = jsonTorrent.optInt("id"),
                hash = jsonTorrent.nullString("hash"),
                leechers = jsonTorrent.optInt("leechers"),
                seeders = jsonTorrent.optInt("seeders"),
                completed = jsonTorrent.optInt("completed"),
                quality = jsonTorrent.nullString("quality"),
                series = jsonTorrent.nullString("series"),
                size = jsonTorrent.optLong("size"),
                url = jsonTorrent.nullString("url")
            )
        }


        val release = ReleaseResponse(
            id = jsonResponse.getInt("id"),
            code = jsonResponse.nullString("code"),
            names = jsonResponse.getJSONArray("names")?.mapStrings { it },
            series = jsonResponse.nullString("series"),
            poster = jsonResponse.nullString("poster"),
            torrentUpdate = jsonResponse.nullString("last")?.toIntOrNull(),
            status = jsonResponse.nullString("status"),
            statusCode = jsonResponse.nullString("statusCode"),
            type = jsonResponse.nullString("type"),
            genres = jsonResponse.optJSONArray("genres")?.mapStrings { it },
            voices = jsonResponse.optJSONArray("voices")?.mapStrings { it },
            year = jsonResponse.nullString("year"),
            season = jsonResponse.nullString("season"),
            scheduleDay = jsonResponse.nullString("day"),
            description = jsonResponse.nullString("description")?.trim(),
            announce = jsonResponse.nullString("announce")?.trim(),
            favoriteInfo = favoriteInfo,
            showDonateDialog = jsonResponse.optBoolean("showDonateDialog"),
            blockedInfo = blockedInfo,
            moonwalkLink = jsonResponse.nullString("moon"),
            episodes = onlineEpisodes,
            externalPlaylists = externalPlaylists,
            torrents = torrents
        )
        return release
    }

}
