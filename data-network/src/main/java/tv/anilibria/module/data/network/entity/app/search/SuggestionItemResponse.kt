package tv.anilibria.module.data.network.entity.app.search

data class SuggestionItemResponse(
    val id: Int,
    val code: String,
    val names: List<String>,
    val poster: String?
)