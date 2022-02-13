package tv.anilibria.module.data.network

import okhttp3.Cookie

/**
 * Created by radiationx on 30.12.17.
 */
interface CookieHolder {
    fun getCookies(): Map<String, Cookie>
    fun putCookie(url: String, cookie: Cookie)
    fun removeCookie(name: String)
}