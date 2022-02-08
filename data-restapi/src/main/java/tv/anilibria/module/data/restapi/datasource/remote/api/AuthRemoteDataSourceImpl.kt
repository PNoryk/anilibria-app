package tv.anilibria.module.data.restapi.datasource.remote.api

import android.net.Uri
import com.squareup.moshi.Moshi
import io.reactivex.Completable
import io.reactivex.Single
import org.json.JSONObject
import ru.radiationx.shared.ktx.android.nullString
import tv.anilibria.module.data.restapi.ApiClient
import tv.anilibria.module.data.restapi.datasource.remote.ApiConfigProvider
import tv.anilibria.module.data.restapi.datasource.remote.ApiError
import tv.anilibria.module.data.restapi.datasource.remote.IClient
import tv.anilibria.module.data.restapi.datasource.remote.mapApiResponse
import tv.anilibria.module.data.restapi.datasource.remote.parsers.AuthParser
import tv.anilibria.module.data.restapi.entity.app.auth.OtpInfoResponse
import tv.anilibria.module.data.restapi.entity.app.auth.SocialAuthException
import tv.anilibria.module.data.restapi.entity.app.auth.SocialAuthServiceResponse
import tv.anilibria.module.data.restapi.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.auth.OtpInfo
import tv.anilibria.module.domain.entity.auth.SocialAuthService
import tv.anilibria.module.domain.remote.AuthRemoteDataSource
import javax.inject.Inject

/**
 * Created by radiationx on 30.12.17.
 */
class AuthRemoteDataSourceImpl @Inject constructor(
    @ApiClient private val client: IClient,
    private val authParser: AuthParser,
    private val apiConfig: ApiConfigProvider,
    private val moshi: Moshi
) : AuthRemoteDataSource {

    override fun loadOtpInfo(deviceId: String): Single<OtpInfo> {
        val args = mapOf(
            "query" to "auth_get_otp",
            "deviceId" to deviceId
        )
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse<OtpInfoResponse>(moshi)
            .onErrorResumeNext { Single.error(authParser.checkOtpError(it)) }
            .map { it.toDomain() }
    }

    override fun acceptOtp(code: String): Completable {
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

    override fun signInOtp(code: String, deviceId: String): Completable {
        val args = mapOf(
            "query" to "auth_login_otp",
            "deviceId" to deviceId,
            "code" to code
        )
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse<Unit>(moshi)
            .onErrorResumeNext { Single.error(authParser.checkOtpError(it)) }
            .ignoreElement()
    }

    override fun signIn(login: String, password: String, code2fa: String): Completable {
        val args = mapOf(
            "mail" to login,
            "passwd" to password,
            "fa2code" to code2fa
        )
        val url = "${apiConfig.baseUrl}/public/login.php"
        return client
            .post(url, args)
            .map { authParser.authResult(it) }
            .ignoreElement()
    }

    override fun loadSocialAuth(): Single<List<SocialAuthService>> {
        val args = mapOf(
            "query" to "social_auth"
        )
        return client
            .post(apiConfig.apiUrl, args)
            .mapApiResponse<List<SocialAuthServiceResponse>>(moshi)
            .map { items -> items.map { it.toDomain() } }
    }

    override fun signInSocial(resultUrl: String, item: SocialAuthService): Completable {
        val fixedUrl = Uri.parse(apiConfig.baseUrl).host?.let { redirectDomain ->
            resultUrl.replace("www.anilibria.tv", redirectDomain)
        } ?: resultUrl

        return client
            .getFull(fixedUrl, emptyMap())
            .doOnSuccess { response ->
                if (item.errorUrlPattern.containsMatchIn(response.redirect)) {
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
            .ignoreElement()
    }

    override fun signOut(): Single<String> {
        return client.post("${apiConfig.baseUrl}/public/logout.php", emptyMap())
    }

}