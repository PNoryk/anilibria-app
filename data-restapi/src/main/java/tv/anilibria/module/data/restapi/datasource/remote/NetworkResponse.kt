package tv.anilibria.module.data.restapi.datasource.remote

data class NetworkResponse(
    var url: String,
    var code: Int = 0,
    var message: String? = null,
    var redirect: String = url,
    var body: String? = null,
    var hostIp: String? = null
)
