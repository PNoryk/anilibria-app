package tv.anilibria.feature.user.data.remote

import retrofit2.Retrofit
import toothpick.InjectConstructor
import tv.anilibria.plugin.data.network.*

@InjectConstructor
class UserApiWrapper(
    private val configHash: ConfigHash,
    @ProxyNetworkQualifier private val proxyProvider: NetworkAwareProvider<Retrofit>,
    @DirectNetworkQualifier private val directProvider: NetworkAwareProvider<Retrofit>,
) : ApiWrapper<UserApi>(configHash, proxyProvider, directProvider) {

    override val apiClass: Class<UserApi> = UserApi::class.java
}