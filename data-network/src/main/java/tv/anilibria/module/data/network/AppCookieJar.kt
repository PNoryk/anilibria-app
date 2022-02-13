package tv.anilibria.module.data.network

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import javax.inject.Inject

class AppCookieJar @Inject constructor(
    private val cookieHolder: CookieHolder,
    private val userHolder: UserHolder
) : CookieJar {

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        var authDestroyed = false
        for (cookie in cookies) {
            if (cookie.value() == "deleted") {
                if (cookie.name() == CookieHolder.PHPSESSID) {
                    authDestroyed = true
                }
                cookieHolder.removeCookie(cookie.name())
            } else {
                cookieHolder.putCookie(url.toString(), cookie)
            }
        }
        if (authDestroyed) {
            userHolder.delete()
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieHolder.getCookies().values.map { it }
    }
}