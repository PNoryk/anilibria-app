package tv.anilibria.plugin.data.network

import javax.inject.Inject
import javax.inject.Provider

class NetworkAwareProvider<T> @Inject constructor(
    private val configHash: ConfigHash,
    private val provider: Provider<T>
) : Provider<T> {

    private var currentValue: T? = null
    private var currentHash: Int? = null

    @Synchronized
    override fun get(): T {
        val oldHash = currentHash
        val newHash = configHash.getHash()
        if (oldHash != newHash) {
            currentHash = newHash
            currentValue = provider.get()
        }
        return requireNotNull(currentValue) {
            "NetworkAwareProvider can not return null"
        }
    }
}