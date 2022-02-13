package tv.anilibria.module.data.repos

import kotlinx.coroutines.flow.Flow
import tv.anilibria.module.data.AuthHolder
import tv.anilibria.module.data.local.holders.SocialAuthLocalDataSource
import tv.anilibria.module.data.restapi.datasource.remote.api.AuthRemoteDataSource
import tv.anilibria.module.domain.entity.auth.OtpInfo
import tv.anilibria.module.domain.entity.auth.SocialAuthService
import javax.inject.Inject

/**
 * Created by radiationx on 30.12.17.
 */
class AuthRepository @Inject constructor(
    private val authApi: AuthRemoteDataSource,
    private val authLocalDataSource: SocialAuthLocalDataSource,
    private val authHolder: AuthHolder
) {

    suspend fun getOtpInfo(): OtpInfo {
        return authApi.loadOtpInfo(authHolder.getDeviceId())
    }

    suspend fun acceptOtp(code: String) {
        authApi.acceptOtp(code)
    }

    suspend fun signInOtp(code: String) {
        authApi.signInOtp(code, authHolder.getDeviceId())
    }

    suspend fun signIn(login: String, password: String, code2fa: String) {
        authApi.signIn(login, password, code2fa)
    }

    suspend fun signOut() {
        authApi.signOut()
    }

    fun observeSocialAuth(): Flow<List<SocialAuthService>> {
        return authLocalDataSource.observe()
    }

    suspend fun loadSocialAuth(): List<SocialAuthService> {
        return authApi.loadSocialAuth().also {
            authLocalDataSource.put(it)
        }
    }

    suspend fun getSocialAuth(key: String): SocialAuthService {
        val cachedService = authLocalDataSource.get().firstOrNull { it.key == key }
        val result = cachedService ?: loadSocialAuth().firstOrNull { it.key == key }
        return requireNotNull(result) {
            "Not found social service by key $key"
        }
    }

    suspend fun signInSocial(resultUrl: String, item: SocialAuthService) {
        authApi.signInSocial(resultUrl, item)
    }
}