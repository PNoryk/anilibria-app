package tv.anilibria.module.data.network.datasource.holders

interface DownloadsHolder {

    fun getDownloads(): List<Long>
    fun saveDownloads(items: List<Long>)
}