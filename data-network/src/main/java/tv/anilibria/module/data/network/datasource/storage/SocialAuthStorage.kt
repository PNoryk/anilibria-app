package tv.anilibria.module.data.network.datasource.storage

import android.content.SharedPreferences
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import org.json.JSONArray
import org.json.JSONObject
import tv.anilibria.module.data.network.DataPreferences
import tv.anilibria.module.data.network.datasource.holders.SocialAuthHolder
import tv.anilibria.module.data.network.entity.app.auth.SocialAuthServiceResponse
import javax.inject.Inject

class SocialAuthStorage @Inject constructor(
        @DataPreferences private val sharedPreferences: SharedPreferences
) : SocialAuthHolder {

    private val dataRelay = BehaviorRelay.createDefault(getSavedData())

    override fun get(): List<SocialAuthServiceResponse> = dataRelay.value!!

    override fun observe(): Observable<List<SocialAuthServiceResponse>> = dataRelay.hide()

    override fun save(items: List<SocialAuthServiceResponse>) {
        saveData(items)
        dataRelay.accept(items)
    }

    override fun delete() {
        sharedPreferences.edit().remove("social_auth").apply()
        dataRelay.accept(emptyList())
    }

    private fun getSavedData(): List<SocialAuthServiceResponse> {
        val resultItems = mutableListOf<SocialAuthServiceResponse>()
        sharedPreferences.getString("social_auth", null)?.also {
            val itemsJson = JSONArray(it)
            for (j in 0 until itemsJson.length()) {
                val jsonItem = itemsJson.getJSONObject(j)
                resultItems.add(SocialAuthServiceResponse(
                        jsonItem.getString("key"),
                        jsonItem.getString("title"),
                        jsonItem.getString("socialUrl"),
                        jsonItem.getString("resultPattern"),
                        jsonItem.getString("errorUrlPattern")
                ))
            }
        }
        return resultItems
    }

    private fun saveData(items: List<SocialAuthServiceResponse>) {
        val resultJson = JSONArray()
        items.forEach { item ->
            resultJson.put(JSONObject().apply {
                put("key", item.key)
                put("title", item.title)
                put("socialUrl", item.socialUrl)
                put("resultPattern", item.resultPattern)
                put("errorUrlPattern", item.errorUrlPattern)
            })
        }
        sharedPreferences.edit().putString("social_auth", resultJson.toString()).apply()
    }

}