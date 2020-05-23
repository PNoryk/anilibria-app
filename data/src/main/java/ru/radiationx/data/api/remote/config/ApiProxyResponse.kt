package ru.radiationx.data.api.remote.config

import com.google.gson.annotations.SerializedName

data class ApiProxyResponse(
    @SerializedName("tag") val tag: String,
    @SerializedName("name") val name: String?,
    @SerializedName("desc") val desc: String?,
    @SerializedName("ip") val ip: String,
    @SerializedName("port") val port: Int,
    @SerializedName("user") val user: String?,
    @SerializedName("password") val password: String?
)