package ru.radiationx.data.adb.datasource.converters

import ru.radiationx.data.adb.entity.release.BlockInfoDb
import ru.radiationx.data.adb.entity.release.FavoriteInfoDb
import ru.radiationx.data.adb.entity.release.FlatReleaseDb
import ru.radiationx.data.adb.entity.release.ReleaseDb
import ru.radiationx.data.adomain.release.BlockInfo
import ru.radiationx.data.adomain.release.FavoriteInfo
import ru.radiationx.data.adomain.release.Release
import toothpick.InjectConstructor

@InjectConstructor
class ReleaseConverter {

    fun toDomain(source: ReleaseDb): Release {
        val favorite = source.favorite?.let { toDomain(it) }
        val blockInfo = source.blockedInfo?.let { toDomain(it) }
        return source.release.run {
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

    fun toDomain(source: FavoriteInfoDb) = FavoriteInfo(
        rating = source.rating,
        added = source.added
    )

    fun toDomain(source: BlockInfoDb) = BlockInfo(
        blocked = source.blocked,
        reason = source.reason
    )

    fun toDb(source: Release): ReleaseDb {
        val favoriteInfoDb = source.favorite?.let { toDb(source.id, it) }
        val blockedInfoDb = source.blockedInfo?.let { toDb(source.id, it) }
        val releaseDb = FlatReleaseDb(
            id = source.id,
            code = source.code,
            nameRu = source.nameRu,
            nameEn = source.nameEn,
            series = source.series,
            poster = source.poster,
            last = source.last,
            webPlayer = source.webPlayer,
            status = source.status,
            type = source.type,
            genres = source.genres,
            voices = source.voices,
            year = source.year,
            day = source.day,
            description = source.description,
            announce = source.announce,
            showDonateDialog = source.showDonateDialog
        )
        return ReleaseDb(releaseDb, favoriteInfoDb, blockedInfoDb)
    }

    fun toDb(releaseId: Int, source: FavoriteInfo) = FavoriteInfoDb(
        releaseId = releaseId,
        rating = source.rating,
        added = source.added
    )

    fun toDb(releaseId: Int, source: BlockInfo) = BlockInfoDb(
        releaseId = releaseId,
        blocked = source.blocked,
        reason = source.reason
    )


    fun toDomain(source: List<ReleaseDb>) = source.map { toDomain(it) }

    fun toDb(source: List<Release>) = source.map { toDb(it) }
}