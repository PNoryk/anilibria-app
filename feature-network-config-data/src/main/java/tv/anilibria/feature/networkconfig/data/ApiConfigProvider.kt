package tv.anilibria.feature.networkconfig.data

import tv.anilibria.feature.networkconfig.data.response.ApiProxyResponse

interface ApiConfigProvider {
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