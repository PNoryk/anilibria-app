package tv.anilibria.module.data.network.entity.mapper

import tv.anilibria.module.data.network.datasource.remote.address.ApiAddressResponse
import tv.anilibria.module.data.network.datasource.remote.address.ApiConfig
import tv.anilibria.module.data.network.datasource.remote.address.ApiConfigResponse
import tv.anilibria.module.data.network.datasource.remote.address.ApiProxyResponse
import tv.anilibria.module.domain.entity.address.ApiAddress
import tv.anilibria.module.domain.entity.address.ApiProxy

fun ApiConfigResponse.toDomain() = ApiConfig(addresses = addresses.map { it.toDomain() })

fun ApiAddressResponse.toDomain() = ApiAddress(
    tag = tag,
    name = name,
    desc = desc,
    widgetsSite = widgetsSite,
    site = site,
    baseImages = baseImages,
    base = base,
    api = api,
    ips = ips,
    proxies = proxies.map { it.toDomain() }
)

fun ApiProxyResponse.toDomain() = ApiProxy(
    tag = tag,
    name = name,
    desc = desc,
    ip = ip,
    port = port,
    user = user,
    password = password
)