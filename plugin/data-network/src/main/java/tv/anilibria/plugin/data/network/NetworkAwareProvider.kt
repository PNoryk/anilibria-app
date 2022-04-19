package tv.anilibria.plugin.data.network

import toothpick.InjectConstructor
import javax.inject.Provider

@InjectConstructor
class NetworkAwareProvider<T>(
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