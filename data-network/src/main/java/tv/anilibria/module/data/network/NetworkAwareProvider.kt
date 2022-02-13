package tv.anilibria.module.data.network

import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Provider

class NetworkAwareProvider<T> @Inject constructor(
    private val provider: Provider<T>
) : Provider<T> {

    private val needUpdate = AtomicBoolean(true)

    private var currentValue: T? = null

    @Synchronized
    fun update() {
        needUpdate.set(true)
    }

    @Synchronized
    override fun get(): T {
        val oldValue = currentValue
        if (needUpdate.compareAndSet(true, false) || oldValue == null) {
            val newClient = provider.get()
            currentValue = newClient
        }
        return requireNotNull(currentValue) {
            "DynamicOkHttpClient can not return null"
        }
    }
}