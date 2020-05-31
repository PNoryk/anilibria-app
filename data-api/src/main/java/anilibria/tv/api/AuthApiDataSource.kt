package anilibria.tv.api

import io.reactivex.Completable
import io.reactivex.Single
import anilibria.tv.domain.entity.auth.OtpInfo
import anilibria.tv.domain.entity.auth.SocialService

interface AuthApiDataSource {
    fun signIn(login: String, password: String, code2fa: String): Completable
    fun signOut(): Completable
    fun getSocialServices(): Single<List<SocialService>>
    fun signInSocial(resultUrl: String): Completable
    fun getOtpInfo(deviceId: String): Single<OtpInfo>
    fun acceptOtp(code: String): Completable
    fun signInOtp(code: String, deviceId: String): Completable
}