package tv.anilibria.feature.vkcomments.data.remote

import retrofit2.Retrofit
import toothpick.InjectConstructor
import tv.anilibria.plugin.data.network.*

@InjectConstructor
class VkCommentsApiWrapper(
    private val configHash: ConfigHash,
    @ProxyNetworkQualifier private val proxyProvider: NetworkAwareProvider<Retrofit>,
    @DirectNetworkQualifier private val directProvider: NetworkAwareProvider<Retrofit>,
) : ApiWrapper<VkCommentsApi>(configHash, proxyProvider, directProvider) {

    override val apiClass: Class<VkCommentsApi> = VkCommentsApi::class.java
}