package ru.radiationx.data.api.datasource

import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adomain.entity.auth.OtpInfo
import ru.radiationx.data.adomain.entity.auth.SocialService
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.converter.AuthConverter
import ru.radiationx.data.api.service.AuthService
import toothpick.InjectConstructor

//todo Авторизация работать не будет, нужно запилить поддержку на стороне апи и так-же в клиенте замутить полноценную поддержку completable
@InjectConstructor
class AuthDataSource(
    private val authService: AuthService,
    private val authConverter: AuthConverter
) {

    fun signIn(login: String, password: String, code2fa: String): Completable = authService
        .signIn(
            mapOf(
                "query" to "login",
                "mail" to login,
                "passwd" to password,
                "fa2code" to code2fa
            )
        )
        .handleApiResponse()
        .ignoreElement()

    fun signOut(): Completable = authService
        .signOut(mapOf("query" to "logout"))
        .handleApiResponse()
        .ignoreElement()


    fun getSocialServices(): Single<List<SocialService>> = authService
        .getSocialServices(mapOf("query" to "social_auth"))
        .handleApiResponse()
        .map { it.map { authConverter.toDomain(it) } }

    fun signInSocial(resultUrl: String): Completable = authService
        .signInSocial(mapOf("query" to "login_social"))
        .handleApiResponse()
        .ignoreElement()


    fun getOtpInfo(deviceId: String): Single<OtpInfo> = authService
        .getOtpInfo(
            mapOf(
                "query" to "auth_get_otp",
                "deviceId" to deviceId
            )
        )
        .handleApiResponse()
        .map { authConverter.toDomain(it) }

    fun acceptOtp(code: String): Completable = authService
        .acceptOtp(
            mapOf(
                "query" to "auth_accept_otp",
                "code" to code
            )
        )
        .handleApiResponse()
        .ignoreElement()

    fun signInOtp(code: String, deviceId: String): Completable = authService
        .signInOtp(
            mapOf(
                "query" to "auth_login_otp",
                "deviceId" to deviceId,
                "code" to code
            )
        )
        .handleApiResponse()
        .ignoreElement()
}