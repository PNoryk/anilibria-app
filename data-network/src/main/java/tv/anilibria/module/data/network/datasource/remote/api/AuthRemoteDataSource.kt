package tv.anilibria.module.data.network.datasource.remote.api

import io.reactivex.Completable
import io.reactivex.Single
import tv.anilibria.module.domain.entity.auth.OtpInfo
import tv.anilibria.module.domain.entity.auth.SocialAuthService

interface AuthRemoteDataSource {
    fun loadOtpInfo(deviceId: String): Single<OtpInfo>
    fun acceptOtp(code: String): Completable
    fun signInOtp(code: String, deviceId: String): Completable
    fun signIn(login: String, password: String, code2fa: String): Completable
    fun loadSocialAuth(): Single<List<SocialAuthService>>
    fun signInSocial(resultUrl: String, item: SocialAuthService): Completable
    fun signOut(): Single<String>
}