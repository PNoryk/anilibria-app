package tv.anilibria.feature.auth.data

import kotlinx.coroutines.flow.Flow
import tv.anilibria.feature.auth.data.domain.OtpInfo
import tv.anilibria.feature.auth.data.domain.SocialAuthService
import tv.anilibria.feature.auth.data.local.DeviceIdLocalDataSource
import tv.anilibria.feature.auth.data.local.SocialAuthLocalDataSource
import tv.anilibria.feature.auth.data.remote.AuthRemoteDataSource

class AuthRepository(
    private val authApi: AuthRemoteDataSource,
    private val authLocalDataSource: SocialAuthLocalDataSource,
    private val deviceIdLocalDataSource: DeviceIdLocalDataSource
) {

    suspend fun getOtpInfo(): OtpInfo {
        return authApi.loadOtpInfo(deviceIdLocalDataSource.get())
    }

    suspend fun acceptOtp(code: String) {
        authApi.acceptOtp(code)
    }

    suspend fun signInOtp(code: String) {
        authApi.signInOtp(code, deviceIdLocalDataSource.get())
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