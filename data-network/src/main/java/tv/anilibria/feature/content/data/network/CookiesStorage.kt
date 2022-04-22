package tv.anilibria.feature.content.data.network

import kotlinx.coroutines.flow.Flow
import okhttp3.Cookie
import okhttp3.HttpUrl
import toothpick.InjectConstructor
import tv.anilibria.plugin.data.storage.InMemoryDataHolder
import tv.anilibria.plugin.data.storage.ObservableData

@InjectConstructor
class CookiesStorage {

    private val data = ObservableData<List<CookieData>>(InMemoryDataHolder(emptyList()))

    fun observe(): Flow<List<CookieData>> = data.observe()

    suspend fun get(): List<CookieData> = data.get()

    suspend fun put(cookie: CookieData) = data.update { cookies ->
        cookies.filter { it.key != cookie.key } + cookie
    }

    suspend fun remove(cookie: CookieData) = data.update { cookies ->
        cookies.filter { it.key != cookie.key }
    }
}

data class CookieData(
    val url: HttpUrl,
    val cookie: Cookie
) {
    val key = url.toString() + cookie.name()
}