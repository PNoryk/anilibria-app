package tv.anilibria.module.data.restapi.datasource.remote

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
}