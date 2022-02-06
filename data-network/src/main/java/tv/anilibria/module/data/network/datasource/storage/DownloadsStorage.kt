package tv.anilibria.module.data.network.datasource.storage

import android.content.SharedPreferences
import tv.anilibria.module.data.network.DataPreferences
import tv.anilibria.module.data.network.datasource.holders.DownloadsHolder
import toothpick.InjectConstructor

@InjectConstructor
class DownloadsStorage(
    @DataPreferences private val sharedPreferences: SharedPreferences
) : DownloadsHolder {

    companion object {
        private const val KEY_DOWNLOADS = "data.download_ids"
    }

    private val downloads = mutableListOf<Long>()

    init {
        sharedPreferences.getString(KEY_DOWNLOADS, null)
            ?.split(",")
            ?.filter { it.isNotEmpty() }
            ?.map { it.toLong() }
            ?.also {
                downloads.addAll(it)
            }
    }

    override fun getDownloads(): List<Long> = downloads.toList()

    override fun saveDownloads(items: List<Long>) {
        sharedPreferences.edit().putString(KEY_DOWNLOADS, items.joinToString(",")).apply()
        downloads.clear()
        downloads.addAll(items)
    }
}