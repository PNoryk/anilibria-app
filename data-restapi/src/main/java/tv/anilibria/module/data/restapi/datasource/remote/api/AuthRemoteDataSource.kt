package tv.anilibria.module.data.restapi.datasource.remote.api

import android.net.Uri
import io.reactivex.Completable
import io.reactivex.Single
import org.json.JSONObject
import retrofit2.HttpException
import tv.anilibria.module.data.restapi.datasource.remote.nullString
import tv.anilibria.module.data.restapi.datasource.remote.parsers.AuthParser
import tv.anilibria.module.data.restapi.datasource.remote.retrofit.AuthApi
import tv.anilibria.module.data.restapi.entity.mapper.toDomain
import tv.anilibria.module.domain.entity.auth.OtpInfo
import tv.anilibria.module.domain.entity.auth.SocialAuthService
import tv.anilibria.module.domain.errors.SocialAuthException
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.restapi.NetworkUrlProvider
import tv.anilibria.plugin.data.restapi.ApiException
import tv.anilibria.plugin.data.restapi.ApiWrapper
import tv.anilibria.plugin.data.restapi.handleApiResponse
import javax.inject.Inject

/**
 * Created by radiationx on 30.12.17.
 */
class AuthRemoteDataSource @Inject constructor(
    private val authParser: AuthParser,
    private val urlProvider: NetworkUrlProvider,
    private val authApi: ApiWrapper<AuthApi>
) {

    fun loadOtpInfo(deviceId: String): Single<OtpInfo> {
        val args = formBodyOf(
            "query" to "auth_get_otp",
            "deviceId" to deviceId
        )
        return authApi.proxy()
            .getOtpInfo(args)
            .handleApiResponse()
            .onErrorResumeNext { Single.error(authParser.checkOtpError(it)) }
            .map { it.toDomain() }
    }

    fun acceptOtp(code: String): Completable {
        val args = formBodyOf(
            "query" to "auth_accept_otp",
            "code" to code
        )
        return authApi.proxy()
            .acceptOtp(args)
            .handleApiResponse()
            .onErrorResumeNext { Single.error(authParser.checkOtpError(it)) }
            .ignoreElement()
    }

    fun signInOtp(code: String, deviceId: String): Completable {
        val args = formBodyOf(
            "query" to "auth_login_otp",
            "deviceId" to deviceId,
            "code" to code
        )
        return authApi.proxy()
            .signInOtp(args)
            .handleApiResponse()
            .onErrorResumeNext { Single.error(authParser.checkOtpError(it)) }
            .ignoreElement()
    }

    fun signIn(login: String, password: String, code2fa: String): Completable {
        val args = formBodyOf(
            "mail" to login,
            "passwd" to password,
            "fa2code" to code2fa
        )
        val url = "${urlProvider.baseUrl}/public/login.php"
        return authApi.proxy()
            .signIn(url, args)
            .map { authParser.authResult(it.string()) }
            .ignoreElement()
    }

    fun loadSocialAuth(): Single<List<SocialAuthService>> {
        val args = formBodyOf(
            "query" to "social_auth"
        )
        return authApi.proxy()
            .getSocialAuthServices(args)
            .handleApiResponse()
            .map { items -> items.map { it.toDomain() } }
    }

    fun signInSocial(resultUrl: String, item: SocialAuthService): Completable {
        val fixedUrl = Uri.parse(urlProvider.baseUrl).host?.let { redirectDomain ->
            resultUrl.replace("www.anilibria.tv", redirectDomain)
        } ?: resultUrl

        return authApi.proxy()
            .signInSocial(fixedUrl)
            .doOnSuccess {
                if (it.isSuccessful) {
                    throw HttpException(it)
                }
            }
            .doOnSuccess { response ->
                val redirect = response.raw().request().url().toString()
                if (item.errorUrlPattern.containsMatchIn(redirect)) {
                    throw SocialAuthException("Social auth result not matched by pattern")
                }
            }
            .doOnSuccess {
                val message = try {
                    JSONObject(it.body()?.string()).nullString("mes")
                } catch (ignore: Exception) {
                    null
                }
                if (message != null) {
                    throw ApiException(400, message, null)
                }
            }
            .ignoreElement()
    }

    fun signOut(): Completable {
        return authApi
            .proxy()
            .signOut("${urlProvider.baseUrl}/public/logout.php")
            .ignoreElement()
    }

}