package ru.radiationx.data.adomain.config

data class ApiProxy(
    val tag: String,
    val name: String?,
    val desc: String?,
    val ip: String,
    val port: Int,
    val user: String?,
    val password: String?
)