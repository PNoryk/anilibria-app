package tv.anilibria.module.data.network

import kotlinx.coroutines.flow.Flow
import okhttp3.Cookie

/**
 * Created by radiationx on 30.12.17.
 */
interface LegacyCookieHolder {
    fun get(): Map<String, Cookie>
    fun put(url: String, cookie: Cookie)
    fun remove(name: String)
}