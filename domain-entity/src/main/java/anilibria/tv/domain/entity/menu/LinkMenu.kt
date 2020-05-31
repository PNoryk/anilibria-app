package anilibria.tv.domain.entity.menu

data class LinkMenu(
    val title: String,
    val absoluteLink: String?,
    val sitePagePath: String?,
    val icon: String?
) {
    companion object {
        const val IC_VK = "vk"
        const val IC_YOUTUBE = "yotube"
        const val IC_PATREON = "patreon"
        const val IC_TELEGRAM = "telegram"
        const val IC_DISCORD = "discord"
        const val IC_ANILIBRIA = "anilibria"
        const val IC_INFO = "info"
        const val IC_RULES = "rules"
        const val IC_PERSON = "person"
        const val IC_SITE = "site"
    }
}