package tv.anilibria.module.data.network.datasource.storage

import android.content.SharedPreferences
import android.util.Log
import org.json.JSONObject
import tv.anilibria.module.data.network.DataPreferences
import tv.anilibria.module.data.network.datasource.remote.address.ApiAddressResponse
import tv.anilibria.module.data.network.datasource.remote.parsers.ConfigurationParser
import javax.inject.Inject

class ApiConfigStorage @Inject constructor(
    @DataPreferences private val sharedPreferences: SharedPreferences,
    private val configurationParser: ConfigurationParser
) {

    companion object {
        private const val KEY_API_CONFIG = "data.apiconfig_v2"
        private const val KEY_API_CONFIG_ACTIVE = "data.apiconfig_active_v2"
    }

    fun saveJson(json: JSONObject) {
        Log.d("bobobo", "ApiConfigStorage saveJson ${json.length()}")
        try {
            sharedPreferences.edit().putString(KEY_API_CONFIG, json.toString()).apply()
        } catch (ex: Throwable) {
            ex.printStackTrace()
        }
    }

    fun get(): List<ApiAddressResponse>? = sharedPreferences
        .getString(KEY_API_CONFIG, null)
        ?.let { configurationParser.parse(JSONObject(it)) }
        .also {
            Log.e("bobobo", "get saved config: ${it?.size}")
        }

    fun setActive(tag: String) =
        sharedPreferences.edit().putString(KEY_API_CONFIG_ACTIVE, tag).apply()

    fun getActive(): String? = sharedPreferences
        .getString(KEY_API_CONFIG_ACTIVE, null)
        .also {
            Log.e("bobobo", "get saved active: ${it}")
        }
}