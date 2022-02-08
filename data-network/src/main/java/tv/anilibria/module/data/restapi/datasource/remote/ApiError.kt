package tv.anilibria.module.data.restapi.datasource.remote

data class ApiError constructor(
    val code: Int?,
    override val message: String?,
    val description: String?
) : RuntimeException()
