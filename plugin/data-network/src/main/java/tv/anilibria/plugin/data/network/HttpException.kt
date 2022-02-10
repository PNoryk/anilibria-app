package tv.anilibria.plugin.data.network

data class HttpException(
    val response: NetworkResponse
) : RuntimeException(response.message)