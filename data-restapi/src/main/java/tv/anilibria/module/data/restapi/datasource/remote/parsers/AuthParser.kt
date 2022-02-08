package tv.anilibria.module.data.restapi.datasource.remote.parsers

import org.json.JSONObject
import ru.radiationx.shared.ktx.android.nullString
import tv.anilibria.module.data.restapi.datasource.remote.ApiException
import tv.anilibria.module.domain.errors.*
import javax.inject.Inject

/**
 * Created by radiationx on 31.12.17.
 */
class AuthParser @Inject constructor() {

    fun checkOtpError(error: Throwable): Throwable = if (error is ApiException) {
        val exceptionMessage = error.message.orEmpty()
        when (error.description) {
            "otpNotFound" -> OtpNotFoundException(exceptionMessage)
            "otpAccepted" -> OtpAcceptedException(exceptionMessage)
            "otpNotAccepted" -> OtpNotAcceptedException(exceptionMessage)
            else -> error
        }
    } else {
        error
    }

    fun authResult(responseText: String): String {
        val responseJson = JSONObject(responseText)
        val error = responseJson.nullString("err")
        val message = responseJson.nullString("mes")
        val key = responseJson.nullString("key")
        if (error != "ok" && key != "authorized") {
            val exceptionMessage = (message ?: key).orEmpty()
            throw when (key) {
                "authorized" -> AlreadyAuthorizedException(exceptionMessage)
                "empty" -> EmptyFieldException(exceptionMessage)
                "wrongUserAgent" -> WrongUserAgentException(exceptionMessage)
                "invalidUser" -> InvalidUserException(exceptionMessage)
                "wrong2FA" -> Wrong2FaCodeException(exceptionMessage)
                "wrongPasswd" -> WrongPasswordException(exceptionMessage)
                else -> ApiException(400, exceptionMessage, null)
            }
        }
        return message.orEmpty()
    }

}