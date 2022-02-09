package tv.anilibria.module.data.local.entity

data class SocialAuthServiceLocal(
    val key: String,
    val title: String,
    val socialUrl: String,
    val resultPattern: String,
    val errorUrlPattern: String,
    val color: String,
    val icon: String
)