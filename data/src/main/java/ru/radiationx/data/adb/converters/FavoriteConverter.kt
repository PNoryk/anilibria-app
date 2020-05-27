package ru.radiationx.data.adb.converters

import ru.radiationx.data.adb.entity.favorite.FavoriteDb
import ru.radiationx.data.adomain.relative.FavoriteRelative
import toothpick.InjectConstructor

@InjectConstructor
class FavoriteConverter {

    fun toDomain(source: FavoriteDb) = FavoriteRelative(source.releaseId)

    fun toDb(source: FavoriteRelative) = FavoriteDb(source.releaseId)


    fun toDomain(source: List<FavoriteDb>) = source.map { toDomain(it) }

    fun toDb(source: List<FavoriteRelative>) = source.map { toDb(it) }
}