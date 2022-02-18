package ru.radiationx.data.datasource.remote

@Deprecated("old data")
interface IApiUtils {
    fun toHtml(text: String?): CharSequence?
    fun escapeHtml(text: String?): String?
}
