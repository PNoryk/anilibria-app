package anilibria.tv.storage.impl.converter

import anilibria.tv.domain.entity.auth.SocialService
import anilibria.tv.domain.entity.menu.LinkMenu
import anilibria.tv.storage.impl.entity.LinkMenuStorage
import anilibria.tv.storage.impl.entity.SocialServiceStorage
import toothpick.InjectConstructor

@InjectConstructor
class SocialServiceConverter {

    fun toDomain(source: SocialServiceStorage) = SocialService(
        key = source.key,
        title = source.title,
        socialUrl = source.socialUrl,
        resultPattern = source.resultPattern,
        errorUrlPattern = source.errorUrlPattern
    )

    fun toStorage(source: SocialService) = SocialServiceStorage(
        key = source.key,
        title = source.title,
        socialUrl = source.socialUrl,
        resultPattern = source.resultPattern,
        errorUrlPattern = source.errorUrlPattern
    )

    fun toDomain(source: List<SocialServiceStorage>) = source.map { toDomain(it) }

    fun toStorage(source: List<SocialService>) = source.map { toStorage(it) }
}