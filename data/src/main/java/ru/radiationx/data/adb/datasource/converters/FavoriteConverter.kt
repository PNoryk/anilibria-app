package ru.radiationx.data.adb.datasource.converters

import ru.radiationx.data.adb.favorite.FavoriteDb
import ru.radiationx.data.adb.favorite.FlatFavoriteDb
import ru.radiationx.data.adomain.release.Release
import toothpick.InjectConstructor

@InjectConstructor
class FavoriteConverter(
    private val releaseConverter: ReleaseConverter
) {

    fun toDomain(favoriteDb: FavoriteDb) = releaseConverter.toDomain(favoriteDb.release)

    fun toDb(release: Release) = FlatFavoriteDb(release.id)


    fun toDomain(items: List<FavoriteDb>) = items.map { toDomain(it) }

    fun toDb(items: List<Release>) = items.map { toDb(it) }
}