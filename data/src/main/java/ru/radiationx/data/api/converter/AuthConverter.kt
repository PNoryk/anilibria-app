package ru.radiationx.data.api.converter

import ru.radiationx.data.adomain.auth.OtpInfo
import ru.radiationx.data.adomain.auth.SocialService
import ru.radiationx.data.api.remote.auth.OtpInfoResponse
import ru.radiationx.data.api.remote.auth.SocialServiceResponse
import ru.radiationx.shared.ktx.dateFromSec
import toothpick.InjectConstructor

@InjectConstructor
class AuthConverter{

    fun toDomain(response: OtpInfoResponse): OtpInfo = OtpInfo(
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