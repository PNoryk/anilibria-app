package tv.anilibria.module.data.restapi.datasource.remote

import tv.anilibria.module.data.restapi.entity.app.address.ApiAddressResponse
import tv.anilibria.module.data.restapi.entity.app.address.ApiProxyResponse

interface ApiConfigProvider {
    val active: ApiAddressResponse
    val tag: String
    val name: String?
    val desc: String?
    val widgetsSiteUrl: String
    val siteUrl: String
    val baseImagesUrl: String
    val baseUrl: String
    val apiUrl: String
    val ips: List<String>
    val proxies: List<ApiProxyResponse>
}