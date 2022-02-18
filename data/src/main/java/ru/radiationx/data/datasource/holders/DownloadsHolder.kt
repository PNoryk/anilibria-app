package ru.radiationx.data.datasource.holders

@Deprecated("old data")
interface DownloadsHolder {

    fun getDownloads(): List<Long>
    fun saveDownloads(items: List<Long>)
}