package ru.radiationx.data.adomain.entity.checker


data class Update(
    val versionCode: String,
    val versionName: String,
    val versionBuild: String,
    val buildDate: String,
    val links: List<UpdateLink>,
    val important: List<String>,
    val added: List<String>,
    val fixed: List<String>,
    val changed: List<String>
)