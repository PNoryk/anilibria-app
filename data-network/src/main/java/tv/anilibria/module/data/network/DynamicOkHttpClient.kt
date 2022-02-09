package tv.anilibria.module.data.network

import okhttp3.OkHttpClient
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Provider

open class DynamicOkHttpClient @Inject constructor(
    private val provider: Provider<OkHttpClient>
) {

    private val needUpdate = AtomicBoolean(true)

    private var currentClient: OkHttpClient? = null

    @Synchronized
    fun update() {
        needUpdate.set(true)
    }

    @Synchronized
    fun get(): OkHttpClient {
        val oldClient = currentClient
        if (needUpdate.compareAndSet(true, false) || oldClient == null) {
            val newClient = provider.get()
            currentClient = newClient
        }
        return requireNotNull(currentClient) {
            "DynamicOkHttpClient can not return null"
        }
    }
}