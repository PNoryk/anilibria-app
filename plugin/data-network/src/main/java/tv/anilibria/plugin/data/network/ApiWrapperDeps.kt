package tv.anilibria.plugin.data.network

import retrofit2.Retrofit
import toothpick.InjectConstructor

@InjectConstructor
class ApiWrapperDeps(
    val configHash: ConfigHash,
    @ProxyNetworkQualifier val proxyProvider: NetworkAwareProvider<Retrofit>,
    @DirectNetworkQualifier val directProvider: NetworkAwareProvider<Retrofit>,
)