package anilibria.tv.db.impl.converters

import anilibria.tv.db.impl.entity.favorite.FavoriteDb
import anilibria.tv.domain.entity.relative.FavoriteRelative
import toothpick.InjectConstructor

@InjectConstructor
class FavoriteConverter {

    fun toDomain(source: FavoriteDb) = FavoriteRelative(source.releaseId)

    fun toDb(source: FavoriteRelative) = FavoriteDb(source.releaseId)


    fun toDomain(source: List<FavoriteDb>) = source.map { toDomain(it) }

    fun toDb(source: List<FavoriteRelative>) = source.map { toDb(it) }
}