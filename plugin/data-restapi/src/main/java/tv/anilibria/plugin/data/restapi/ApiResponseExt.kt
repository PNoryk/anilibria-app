package tv.anilibria.plugin.data.restapi

fun <T> ApiResponse<T>.handleApiResponse(): T = requireNotNull(handle().data) {
    "Data from api response can't be null"
}