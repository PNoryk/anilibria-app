package tv.anilibria.module.data.network.entity.app.auth

data class SocialAuthServiceResponse(
    val key: String,
    val title: String,
    val socialUrl: String,
    val resultPattern: String,
    val errorUrlPattern: String
) {
    companion object {
        const val KEY_VK = "vk"
        const val KEY_PATREON = "patreon"
    }
}