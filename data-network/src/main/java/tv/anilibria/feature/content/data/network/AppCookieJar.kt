package tv.anilibria.feature.content.data.network

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import javax.inject.Inject

class AppCookieJar @Inject constructor(
    private val cookieHolder: LegacyCookieHolder
) : CookieJar {

    companion object {
        private const val COOKIE_DELETED = "deleted"
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        for (cookie in cookies) {
            if (cookie.value() == COOKIE_DELETED) {
                cookieHolder.remove(cookie.name())
            } else {
                cookieHolder.put(url.toString(), cookie)
            }
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieHolder.get().values.map { it }
    }
}