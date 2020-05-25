package ru.radiationx.data.api.service.auth

import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.adomain.auth.OtpInfo
import ru.radiationx.data.adomain.auth.SocialService
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.converter.AuthConverter
import toothpick.InjectConstructor

//todo Авторизация работать не будет, нужно запилить поддержку на стороне апи и так-же в клиенте замутить полноценную поддержку completable
@InjectConstructor
class AuthService(
    private val authApi: AuthApi,
    private val authConverter: AuthConverter
) {

    fun signIn(login: String, password: String, code2fa: String): Completable = authApi
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

    fun signOut(): Completable = authApi
        .signOut(mapOf("query" to "logout"))
        .handleApiResponse()
        .ignoreElement()


    fun getSocialServices(): Single<List<SocialService>> = authApi
        .getSocialServices(mapOf("query" to "social_auth"))
        .handleApiResponse()
        .map { it.map { authConverter.toDomain(it) } }

    fun signInSocial(resultUrl: String): Completable = authApi
        .signInSocial(mapOf("query" to "login_social"))
        .handleApiResponse()
        .ignoreElement()


    fun getOtpInfo(deviceId: String): Single<OtpInfo> = authApi
        .getOtpInfo(
            mapOf(
                "query" to "auth_get_otp",
                "deviceId" to deviceId
            )
        )
        .handleApiResponse()
        .map { authConverter.toDomain(it) }

    fun acceptOtp(code: String): Completable = authApi
        .acceptOtp(
            mapOf(
                "query" to "auth_accept_otp",
                "code" to code
            )
        )
        .handleApiResponse()
        .ignoreElement()

    fun signInOtp(code: String, deviceId: String): Completable = authApi
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