package tv.anilibria.plugin.data.restapi

import io.reactivex.Single

fun <T> Single<ApiResponse<T>>.handleApiResponse(): Single<T> = map {
    val handledResponse = it.handle()
    requireNotNull(handledResponse.data) {
        "Data from api response can't be null"
    }
}

fun <T> ApiResponse<T>.handleApiResponse(): T = requireNotNull(handle().data) {
    "Data from api response can't be null"
}