package ru.radiationx.data.api.service.auth

import io.reactivex.Completable
import io.reactivex.Single
import ru.radiationx.data.api.remote.auth.OtpInfoResponse
import ru.radiationx.data.api.remote.auth.SocialServiceResponse
import ru.radiationx.data.api.common.handleApiResponse

class AuthService(
    private val authApi: AuthApi
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


    fun getSocialServices(): Single<List<SocialServiceResponse>> = authApi
        .getSocialServices(mapOf("query" to "social_auth"))
        .handleApiResponse()

    fun signInSocial(resultUrl: String): Completable = authApi
        .signInSocial(mapOf("query" to "login_social"))
        .handleApiResponse()
        .ignoreElement()


    fun getOtpInfo(deviceId: String): Single<OtpInfoResponse> = authApi
        .getOtpInfo(
            mapOf(
                "query" to "auth_get_otp",
                "deviceId" to deviceId
            )
        )
        .handleApiResponse()

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