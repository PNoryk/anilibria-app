package tv.anilibria.module.data.network.entity.app.updater

/**
 * Created by radiationx on 28.01.18.
 */
data class UpdateDataResponse(
    val code: Int,
    val build: Int,
    val name: String,
    val date: String,
    val links: List<UpdateLinkResponse>,
    val important: List<String>,
    val added: List<String>,
    val fixed: List<String>,
    val changed: List<String>,
)