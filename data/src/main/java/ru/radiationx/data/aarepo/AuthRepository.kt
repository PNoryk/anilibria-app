package ru.radiationx.data.aarepo

import anilibria.tv.api.AuthApiDataSource
import anilibria.tv.api.UserApiDataSource
import anilibria.tv.cache.SocialServiceCache
import anilibria.tv.cache.UserAuthCache
import anilibria.tv.domain.entity.auth.OtpInfo
import anilibria.tv.domain.entity.auth.SocialService
import anilibria.tv.domain.entity.auth.UserAuth
import anilibria.tv.storage.DeviceIdStorageDataSource
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import toothpick.InjectConstructor

@InjectConstructor
class AuthRepository(
    private val authApiDataSource: AuthApiDataSource,
    private val userApiDataSource: UserApiDataSource,
    private val deviceIdStorageDataSource: DeviceIdStorageDataSource,
    private val socialServiceCache: SocialServiceCache,
    private val userAuthCache: UserAuthCache
) {

    fun observeUserAuth(): Observable<UserAuth> = userAuthCache.observeUser()

    fun observeSocialServices(): Observable<List<SocialService>> = socialServiceCache.observeList()

    fun getUserAuth(): Single<UserAuth> = userApiDataSource
        .getSelf()
        .onErrorResumeNext { userAuthCache.deleteUser().andThen(Single.error(it)) }
        .flatMapCompletable { userAuthCache.putUser(it) }
        .andThen(userAuthCache.getUser())

    fun signIn(login: String, password: String, code2fa: String): Single<UserAuth> = authApiDataSource
        .signIn(login, password, code2fa)
        .andThen(getUserAuth())

    fun signInOtp(code: String): Single<UserAuth> = deviceIdStorageDataSource
        .getDeviceId()
        .flatMapCompletable { authApiDataSource.signInOtp(code, it) }
        .andThen(getUserAuth())

    fun signInSocial(resultUrl: String, service: SocialService): Single<UserAuth> = authApiDataSource
        .signInSocial(resultUrl, service)
        .andThen(getUserAuth())

    fun getSocialServices(): Single<List<SocialService>> = authApiDataSource
        .getSocialServices()
        .flatMapCompletable { socialServiceCache.putList(it) }
        .andThen(socialServiceCache.getList())

    fun acceptOtp(code: String): Completable = authApiDataSource.acceptOtp(code)

    fun getOtpInfo(): Single<OtpInfo> = deviceIdStorageDataSource
        .getDeviceId()
        .flatMap { authApiDataSource.getOtpInfo(it) }

    fun signOut(): Completable = authApiDataSource
        .signOut()
        .andThen(userAuthCache.deleteUser())
}