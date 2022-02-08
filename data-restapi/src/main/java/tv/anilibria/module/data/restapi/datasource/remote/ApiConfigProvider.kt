package tv.anilibria.module.data.restapi.datasource.remote

interface ApiConfigProvider {
    val baseUrl: String
    val apiUrl: String
}