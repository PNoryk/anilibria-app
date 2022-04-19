package tv.anilibria.feature.auth.data.remote

import android.net.Uri
import org.json.JSONObject
import retrofit2.HttpException
import toothpick.InjectConstructor
import tv.anilibria.feature.auth.data.domain.OtpInfo
import tv.anilibria.feature.auth.data.domain.SocialAuthService
import tv.anilibria.feature.content.errors.SocialAuthException
import tv.anilibria.plugin.data.network.ApiWrapper
import tv.anilibria.plugin.data.network.NetworkUrlProvider
import tv.anilibria.plugin.data.network.formBodyOf
import tv.anilibria.plugin.data.restapi.ApiException
import tv.anilibria.plugin.data.restapi.handleApiResponse

@InjectConstructor
class AuthRemoteDataSource(
    private val authParser: AuthParser,
    private val urlProvider: NetworkUrlProvider,
    private val authApi: ApiWrapper<AuthApi>
) {

    suspend fun loadOtpInfo(deviceId: String): OtpInfo = try {
        val args = formBodyOf(
            "query" to "auth_get_otp",
            "deviceId" to deviceId
        )
        authApi.proxy()
            .getOtpInfo(args)
            .handleApiResponse()
            .toDomain()
    } catch (ex: Exception) {
        throw authParser.checkOtpError(ex)
    }

    suspend fun acceptOtp(code: String) = try {
        val args = formBodyOf(
            "query" to "auth_accept_otp",
            "code" to code
        )
        authApi.proxy()
            .acceptOtp(args)
            .handleApiResponse()
    } catch (ex: Exception) {
        throw authParser.checkOtpError(ex)
    }

    suspend fun signInOtp(code: String, deviceId: String) = try {
        val args = formBodyOf(
            "query" to "auth_login_otp",
            "deviceId" to deviceId,
            "code" to code
        )
        authApi.proxy()
            .signInOtp(args)
            .handleApiResponse()
    } catch (ex: Exception) {
        throw authParser.checkOtpError(ex)
    }

    suspend fun signIn(login: String, password: String, code2fa: String) {
        val args = formBodyOf(
            "mail" to login,
            "passwd" to password,
            "fa2code" to code2fa
        )
        val url = "${urlProvider.baseUrl}/public/login.php"
        return authApi.proxy()
            .signIn(url, args)
            .let { authParser.authResult(it.string()) }
    }

    suspend fun loadSocialAuth(): List<SocialAuthService> {
        val args = formBodyOf(
            "query" to "social_auth"
        )
        return authApi.proxy()
            .getSocialAuthServices(args)
            .handleApiResponse()
            .map { it.toDomain() }
    }

    suspend fun signInSocial(resultUrl: String, item: SocialAuthService) {
        val fixedUrl = Uri.parse(urlProvider.baseUrl).host?.let { redirectDomain ->
            resultUrl.replace("www.anilibria.tv", redirectDomain)
        } ?: resultUrl

        val response = authApi.proxy().signInSocial(fixedUrl)
        if (response.isSuccessful) {
            throw HttpException(response)
        }

        val redirect = response.raw().request().url().toString()
        if (item.errorUrlPattern.containsMatchIn(redirect)) {
            throw SocialAuthException("Social auth result not matched by pattern")
        }

        val message = try {
            JSONObject(response.body()?.string()).nullString("mes")
        } catch (ignore: Exception) {
            null
        }
        if (message != null) {
            throw ApiException(400, message, null)
        }
    }

    suspend fun signOut() {
        authApi.proxy().signOut("${urlProvider.baseUrl}/public/logout.php")
    }

}