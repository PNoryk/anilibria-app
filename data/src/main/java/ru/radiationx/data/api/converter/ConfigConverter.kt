package ru.radiationx.data.api.converter

import anilibria.tv.domain.entity.config.ApiAddress
import anilibria.tv.domain.entity.config.ApiProxy
import ru.radiationx.data.api.entity.config.ApiAddressResponse
import ru.radiationx.data.api.entity.config.ApiProxyResponse
import toothpick.InjectConstructor

@InjectConstructor
class ConfigConverter {

    fun toDomain(response: ApiAddressResponse) = ApiAddress(
        tag = response.tag,
        name = response.name,
        desc = response.desc,
        widgetsSite = response.widgetsSite,
        site = response.site,
        baseImages = response.baseImages,
        base = response.base,
        api = response.api,
        ips = response.ips,
        proxies = response.proxies.map { toDomain(it) }
    )

    fun toDomain(response: ApiProxyResponse) = ApiProxy(
        tag = response.tag,
        name = response.name,
        desc = response.desc,
        ip = response.ip,
        port = response.port,
        user = response.user,
        password = response.password
    )
}