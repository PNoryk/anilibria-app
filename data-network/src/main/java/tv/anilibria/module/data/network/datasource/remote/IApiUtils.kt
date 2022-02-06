package tv.anilibria.module.data.network.datasource.remote

interface IApiUtils {
    fun toHtml(text: String?): CharSequence?
    fun escapeHtml(text: String?): String?
}
