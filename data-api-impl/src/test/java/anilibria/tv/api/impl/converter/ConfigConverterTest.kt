package anilibria.tv.api.impl.converter

import anilibria.tv.api.impl.entity.checker.UpdateLinkResponse
import anilibria.tv.api.impl.entity.checker.UpdateResponse
import anilibria.tv.api.impl.entity.comments.CommentsInfoResponse
import anilibria.tv.api.impl.entity.config.ApiAddressResponse
import anilibria.tv.api.impl.entity.config.ApiProxyResponse
import anilibria.tv.domain.entity.checker.Update
import anilibria.tv.domain.entity.checker.UpdateLink
import anilibria.tv.domain.entity.comments.CommentsInfo
import anilibria.tv.domain.entity.config.ApiAddress
import anilibria.tv.domain.entity.config.ApiProxy
import org.junit.Assert.assertEquals
import org.junit.Test

class ConfigConverterTest {

    private val converter = ConfigConverter()

    private val responseProxies = listOf(
        ApiProxyResponse(
            tag = "proxyTag1",
            name = "proxyName1",
            desc = null,
            ip = "proxyIp1",
            port = 80,
            user = "user",
            password = "password"
        ),
        ApiProxyResponse(
            tag = "proxyTag2",
            name = "proxyName2",
            desc = "proxyDesc2",
            ip = "proxyIp2",
            port = 80,
            user = null,
            password = null
        )
    )

    private val domainProxies = listOf(
        ApiProxy(
            tag = "proxyTag1",
            name = "proxyName1",
            desc = null,
            ip = "proxyIp1",
            port = 80,
            user = "user",
            password = "password"
        ),
        ApiProxy(
            tag = "proxyTag2",
            name = "proxyName2",
            desc = "proxyDesc2",
            ip = "proxyIp2",
            port = 80,
            user = null,
            password = null
        )
    )

    private val response = listOf(
        ApiAddressResponse(
            tag = "tag1",
            name = "name1",
            desc = "desc1",
            widgetsSite = "website1",
            site = "site1",
            baseImages = "baseImages1",
            base = "base1",
            api = "api1",
            ips = listOf("ip1", "ip2"),
            proxies = responseProxies
        ),
        ApiAddressResponse(
            tag = "tag2",
            name = null,
            desc = null,
            widgetsSite = "website2",
            site = "site2",
            baseImages = "baseImages2",
            base = "base2",
            api = "api2",
            ips = listOf("ip1", "ip2"),
            proxies = responseProxies
        )
    )

    private val domain = listOf(
        ApiAddress(
            tag = "tag1",
            name = "name1",
            desc = "desc1",
            widgetsSite = "website1",
            site = "site1",
            baseImages = "baseImages1",
            base = "base1",
            api = "api1",
            ips = listOf("ip1", "ip2"),
            proxies = domainProxies
        ),
        ApiAddress(
            tag = "tag2",
            name = null,
            desc = null,
            widgetsSite = "website2",
            site = "site2",
            baseImages = "baseImages2",
            base = "base2",
            api = "api2",
            ips = listOf("ip1", "ip2"),
            proxies = domainProxies
        )
    )

    @Test
    fun `from response EXPECT domain`() {
        val actual = response.map { converter.toDomain(it) }
        assertEquals(domain, actual)
    }
}