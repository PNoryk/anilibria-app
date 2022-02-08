package tv.anilibria.module.data.network

data class HttpException(
    val response: NetworkResponse
) : RuntimeException(response.message)