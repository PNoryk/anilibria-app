package ru.radiationx.data.adb.datasource.converters

import ru.radiationx.data.adb.release.BlockInfoDb
import ru.radiationx.data.adb.release.FavoriteInfoDb
import ru.radiationx.data.adb.release.FlatReleaseDb
import ru.radiationx.data.adb.release.ReleaseDb
import ru.radiationx.data.adomain.release.BlockInfo
import ru.radiationx.data.adomain.release.FavoriteInfo
import ru.radiationx.data.adomain.release.Release
import toothpick.InjectConstructor

@InjectConstructor
class ReleaseConverter {

    fun toDomain(releaseDb: ReleaseDb): Release {
        val favorite = releaseDb.favorite?.let { toDomain(it) }
        val blockInfo = releaseDb.blockedInfo?.let { toDomain(it) }
        return releaseDb.release.run {
            Release(
                id = id,
                code = code,
                nameRu = nameRu,
                nameEn = nameEn,
                series = series,
                poster = poster,
                favorite = favorite,
                last = last,
                webPlayer = webPlayer,
                status = status,
                type = type,
                genres = genres,
                voices = voices,
                year = year,
                day = day,
                description = description,
                announce = announce,
                blockedInfo = blockInfo,
                playlist = null,
                torrents = null,
                showDonateDialog = showDonateDialog
            )
        }
    }

    fun toDomain(favoriteInfoDb: FavoriteInfoDb) = FavoriteInfo(
        rating = favoriteInfoDb.rating,
        added = favoriteInfoDb.added
    )

    fun toDomain(blockInfoDb: BlockInfoDb) = BlockInfo(
        blocked = blockInfoDb.blocked,
        reason = blockInfoDb.reason
    )

    fun toDb(release: Release): ReleaseDb {
        val favoriteInfoDb = release.favorite?.let { toDb(release.id, it) }
        val blockedInfoDb = release.blockedInfo?.let { toDb(release.id, it) }
        val releaseDb = FlatReleaseDb(
            id = release.id,
            code = release.code,
            nameRu = release.nameRu,
            nameEn = release.nameEn,
            series = release.series,
            poster = release.poster,
            last = release.last,
            webPlayer = release.webPlayer,
            status = release.status,
            type = release.type,
            genres = release.genres,
            voices = release.voices,
            year = release.year,
            day = release.day,
            description = release.description,
            announce = release.announce,
            showDonateDialog = release.showDonateDialog
        )
        return ReleaseDb(releaseDb, favoriteInfoDb, blockedInfoDb)
    }

    fun toDb(releaseId: Int, favoriteInfo: FavoriteInfo) = FavoriteInfoDb(
        releaseId = releaseId,
        rating = favoriteInfo.rating,
        added = favoriteInfo.added
    )

    fun toDb(releaseId: Int, blockInfo: BlockInfo) = BlockInfoDb(
        releaseId = releaseId,
        blocked = blockInfo.blocked,
        reason = blockInfo.reason
    )


    fun toDomain(items: List<ReleaseDb>) = items.map { toDomain(it) }

    fun toDb(items: List<Release>) = items.map { toDb(it) }
}