package tv.anilibria.module.domain.entity.auth

data class SocialAuthService(
    val key: String,
    val title: String,
    val socialUrl: String,
    val resultPattern: String,
    val errorUrlPattern: String
)