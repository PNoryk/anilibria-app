package ru.radiationx.data.repository

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import ru.radiationx.shared.ktx.SchedulersProvider
import ru.radiationx.data.datasource.holders.AuthHolder
import ru.radiationx.data.datasource.holders.SocialAuthHolder
import ru.radiationx.data.datasource.holders.UserHolder
import ru.radiationx.data.datasource.remote.ApiError
import ru.radiationx.data.datasource.remote.api.AuthApi
import ru.radiationx.data.entity.app.auth.OtpInfo
import ru.radiationx.data.entity.app.auth.SocialAuth
import ru.radiationx.data.entity.app.other.ProfileItem
import ru.radiationx.data.entity.common.AuthState
import ru.radiationx.data.system.HttpException
import javax.inject.Inject

/**
 * Created by radiationx on 30.12.17.
 */
@Deprecated("old data")
class AuthRepository @Inject constructor(
    private val schedulers: SchedulersProvider,
    private val authApi: AuthApi,
    private val userHolder: UserHolder,
    private val authHolder: AuthHolder,
    private val socialAuthHolder: SocialAuthHolder
) {

    /*private val socialAuthInfo = listOf(SocialAuth(
            "vk",
            "ВКонтакте",
            "https://oauth.vk.com/authorize?client_id=5315207&redirect_uri=https://www.anilibria.tv/public/vk.php",
            "https?:\\/\\/(?:(?:www|api)?\\.)?anilibria\\.tv\\/public\\/vk\\.php([?&]code)",
            "https?:\\/\\/(?:(?:www|api)?\\.)?anilibria\\.tv\\/pages\\/vk\\.php"
    ))*/

    fun observeUser(): Observable<ProfileItem> = userHolder
        .observeUser()
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    fun getUser() = userHolder.getUser()

    fun getAuthState(): AuthState = userHolder.getUser().authState

    fun updateUser(authState: AuthState) {
        val user = userHolder.getUser()
        user.authState = authState
        userHolder.saveUser(user)
    }

    private fun updateUser(newUser: ProfileItem) {
        newUser.authState = AuthState.AUTH
        userHolder.saveUser(newUser)
    }

    // охеренный метод, которым проверяем авторизацию и одновременно подтягиваем юзера. двойной профит.
    fun loadUser(): Single<ProfileItem> = authApi
        .loadUser()
        .doOnSuccess { updateUser(it) }
        .doOnError {
            it.printStackTrace()
            val code = ((it as? ApiError)?.code ?: (it as? HttpException)?.code)
            if (code == 401) {
                userHolder.delete()
            }
        }
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    fun getOtpInfo(): Single<OtpInfo> = authApi
        .loadOtpInfo(authHolder.getDeviceId())
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    fun signInOtp(code: String): Single<ProfileItem> = authApi
        .signInOtp(code, authHolder.getDeviceId())
        .doOnSuccess { userHolder.saveUser(it) }
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    fun signIn(login: String, password: String, code2fa: String): Single<ProfileItem> = authApi
        .signIn(login, password, code2fa)
        .doOnSuccess { userHolder.saveUser(it) }
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    fun signOut(): Single<String> = authApi
        .signOut()
        .doOnSuccess {
            userHolder.delete()
        }
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

}