package anilibria.tv.api.impl.converter

import anilibria.tv.api.impl.common.dateFromSec
import anilibria.tv.domain.entity.auth.OtpInfo
import anilibria.tv.domain.entity.auth.SocialService
import anilibria.tv.api.impl.entity.auth.OtpInfoResponse
import anilibria.tv.api.impl.entity.auth.SocialServiceResponse
import toothpick.InjectConstructor

@InjectConstructor
class AuthConverter{

    fun toDomain(response: OtpInfoResponse): OtpInfo =
        OtpInfo(
            code = response.code,
            description = response.description,
            expiresAt = response.expiresAt.dateFromSec(),
            remainingTime = response.remainingTime
        )

    fun toDomain(response: SocialServiceResponse) = SocialService(
        key = response.key,
        title = response.title,
        socialUrl = response.socialUrl,
        resultPattern = response.resultPattern,
        errorUrlPattern = response.errorUrlPattern
    )
}