package tv.anilibria.feature.networkconfig.data

import tv.anilibria.feature.networkconfig.data.domain.ApiAddress
import tv.anilibria.feature.networkconfig.data.domain.ApiConfig
import tv.anilibria.feature.networkconfig.data.domain.ApiProxy
import tv.anilibria.feature.networkconfig.data.response.ApiAddressResponse
import tv.anilibria.feature.networkconfig.data.response.ApiConfigResponse
import tv.anilibria.feature.networkconfig.data.response.ApiProxyResponse

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