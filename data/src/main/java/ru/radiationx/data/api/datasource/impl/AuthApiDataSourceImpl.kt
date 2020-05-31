package ru.radiationx.data.api.datasource.impl

import io.reactivex.Completable
import io.reactivex.Single
import anilibria.tv.domain.entity.auth.OtpInfo
import anilibria.tv.domain.entity.auth.SocialService
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.converter.AuthConverter
import ru.radiationx.data.api.datasource.AuthApiDataSource
import ru.radiationx.data.api.service.AuthService
import toothpick.InjectConstructor

//todo Авторизация работать не будет, нужно запилить поддержку на стороне апи и так-же в клиенте замутить полноценную поддержку completable
@InjectConstructor
class AuthApiDataSourceImpl(
    private val authService: AuthService,
    private val authConverter: AuthConverter
) : AuthApiDataSource {

    override fun signIn(login: String, password: String, code2fa: String): Completable = authService
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

    override fun signOut(): Completable = authService
        .signOut(mapOf("query" to "logout"))
        .handleApiResponse()
        .ignoreElement()


    override fun getSocialServices(): Single<List<SocialService>> = authService
        .getSocialServices(mapOf("query" to "social_auth"))
        .handleApiResponse()
        .map { it.map { authConverter.toDomain(it) } }

    override fun signInSocial(resultUrl: String): Completable = authService
        .signInSocial(mapOf("query" to "login_social"))
        .handleApiResponse()
        .ignoreElement()


    override fun getOtpInfo(deviceId: String): Single<OtpInfo> = authService
        .getOtpInfo(
            mapOf(
                "query" to "auth_get_otp",
                "deviceId" to deviceId
            )
        )
        .handleApiResponse()
        .map { authConverter.toDomain(it) }

    override fun acceptOtp(code: String): Completable = authService
        .acceptOtp(
            mapOf(
                "query" to "auth_accept_otp",
                "code" to code
            )
        )
        .handleApiResponse()
        .ignoreElement()

    override fun signInOtp(code: String, deviceId: String): Completable = authService
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