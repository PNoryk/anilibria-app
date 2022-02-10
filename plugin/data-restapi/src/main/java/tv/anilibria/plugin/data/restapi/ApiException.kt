package tv.anilibria.plugin.data.restapi

data class ApiException constructor(
    val code: Int?,
    override val message: String?,
    val description: String?
) : RuntimeException()
