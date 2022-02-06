package tv.anilibria.module.data.network.datasource.remote.api

import android.net.Uri
import com.squareup.moshi.Moshi
import io.reactivex.Completable
import io.reactivex.Single
import org.json.JSONObject
import ru.radiationx.shared.ktx.android.nullString
import tv.anilibria.module.data.network.ApiClient
import tv.anilibria.module.data.network.datasource.remote.ApiError
import tv.anilibria.module.data.network.datasource.remote.IClient
import tv.anilibria.module.data.network.datasource.remote.address.ApiConfigProvider
import tv.anilibria.module.data.network.datasource.remote.mapApiResponse
import tv.anilibria.module.data.network.datasource.remote.parsers.AuthParser
import tv.anilibria.module.data.network.entity.app.auth.OtpInfoResponse
import tv.anilibria.module.data.network.entity.app.auth.SocialAuthException
import tv.anilibria.module.data.network.entity.app.auth.SocialAuthServiceResponse
import tv.anilibria.module.data.network.entity.app.other.UserResponse
import java.util.regex.Pattern
import javax.inject.Inject

/**
 * Created by radiationx on 30.12.17.
 */
class AuthApi @Inject constructor(
    @ApiClient private val client: IClient,
    private val authParser: AuthParser,
    private val apiConfig: ApiConfigProvider,
    private val moshi: Moshi
) {

    fun loadUser(): Single<UserResponse> {
        val args = mapOf(
            "query" to "user"
        )
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse(moshi)
    }

    fun loadOtpInfo(deviceId: String): Single<OtpInfoResponse> {
        val args = mapOf(
            "query" to "auth_get_otp",
            "deviceId" to deviceId
        )
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse<OtpInfoResponse>(moshi)
            .onErrorResumeNext { Single.error(authParser.checkOtpError(it)) }
    }

    fun acceptOtp(code: String): Completable {
        val args = mapOf(
            "query" to "auth_accept_otp",
            "code" to code
        )
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse<Unit>(moshi)
            .onErrorResumeNext { Single.error(authParser.checkOtpError(it)) }
            .ignoreElement()
    }

    fun signInOtp(code: String, deviceId: String): Single<UserResponse> {
        val args = mapOf(
            "query" to "auth_login_otp",
            "deviceId" to deviceId,
            "code" to code
        )
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse<Unit>(moshi)
            .onErrorResumeNext { Single.error(authParser.checkOtpError(it)) }
            .flatMap { loadUser() }
    }

    fun signIn(login: String, password: String, code2fa: String): Single<UserResponse> {
        val args = mapOf(
            "mail" to login,
            "passwd" to password,
            "fa2code" to code2fa
        )
        val url = "${apiConfig.baseUrl}/public/login.php"
        return client
            .post(url, args)
            .map { authParser.authResult(it) }
            .flatMap { loadUser() }
    }

    fun loadSocialAuth(): Single<List<SocialAuthServiceResponse>> {
        val args = mapOf(
            "query" to "social_auth"
        )
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse(moshi)
    }

    fun signInSocial(resultUrl: String, item: SocialAuthServiceResponse): Single<UserResponse> {
        val fixedUrl = Uri.parse(apiConfig.baseUrl).host?.let { redirectDomain ->
            resultUrl.replace("www.anilibria.tv", redirectDomain)
        } ?: resultUrl

        return client
            .getFull(fixedUrl, emptyMap())
            .doOnSuccess { response ->
                val matcher = Pattern.compile(item.errorUrlPattern).matcher(response.redirect)
                if (matcher.find()) {
                    throw SocialAuthException()
                }
            }
            .doOnSuccess {
                val message = try {
                    JSONObject(it.body).nullString("mes")
                } catch (ignore: Exception) {
                    null
                }
                if (message != null) {
                    throw ApiError(400, message, null)
                }
            }
            .flatMap { loadUser() }
    }

    fun signOut(): Single<String> {
        return client.post("${apiConfig.baseUrl}/public/logout.php", emptyMap())
    }

}