package tv.anilibria.feature.data.network

import android.content.SharedPreferences
import android.util.Log
import okhttp3.Cookie
import okhttp3.HttpUrl
import javax.inject.Inject

/**
 * Created by radiationx on 30.12.17.
 */
//todo change to lazy init and smh else
class LagacyCookiesStorage @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : LegacyCookieHolder {

    private val clientCookies = mutableMapOf<String, Cookie>()

    init {
        sharedPreferences.all.keys
            .filter { it.startsWith("cookie_") }
            .mapNotNull { sharedPreferences.getString(it, null)?.let { parseCookie(it) } }
            .forEach { clientCookies[it.name()] = it }
    }

    private fun parseCookie(cookieFields: String): Cookie? {
        val fields = cookieFields.split("\\|:\\|".toRegex())
        val httpUrl = HttpUrl.parse(fields[0])
            ?: throw RuntimeException("Unknown cookie url = ${fields[0]}")
        val cookieString = fields[1]
        return Cookie.parse(httpUrl, cookieString)
    }

    private fun convertCookie(url: String, cookie: Cookie): String {
        return "$url|:|$cookie"
    }

    override fun get(): Map<String, Cookie> {
        Log.e(
            "CookiesStorage",
            "getCookies: ${
                clientCookies.map { it.value }.joinToString { "${it.name()}=${it.value()}" }
            }"
        )
        return clientCookies
    }

    override fun put(url: String, cookie: Cookie) {
        Log.e("CookiesStorage", "putCookie: ${"${cookie.name()}=${cookie.value()}"}")
        sharedPreferences
            .edit()
            .putString("cookie_${cookie.name()}", convertCookie(url, cookie))
            .apply()

        if (!clientCookies.containsKey(cookie.name())) {
            clientCookies.remove(cookie.name())
        }
        clientCookies[cookie.name()] = cookie
    }

    override fun remove(name: String) {
        sharedPreferences
            .edit()
            .remove("cookie_$name")
            .apply()

        clientCookies.remove(name)
    }
}