package tv.anilibria.feature.content.data.network

import kotlinx.coroutines.runBlocking
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import toothpick.InjectConstructor

@InjectConstructor
class AppCookieJar(
    private val cookieHolder: CookiesStorage
) : CookieJar {

    companion object {
        private const val COOKIE_DELETED = "deleted"
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        runBlocking {
            for (cookie in cookies) {
                if (cookie.value() == COOKIE_DELETED) {
                    cookieHolder.remove(CookieData(url, cookie))
                } else {
                    cookieHolder.put(CookieData(url, cookie))
                }
            }
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return runBlocking {
            cookieHolder.get().map { it.cookie }
        }
    }
}