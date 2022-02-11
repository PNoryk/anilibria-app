package tv.anilibria.plugin.data.network

data class NetworkResponse(
    val url: String,
    val code: Int,
    val message: String,
    val redirect: String,
    val body: String,
)
