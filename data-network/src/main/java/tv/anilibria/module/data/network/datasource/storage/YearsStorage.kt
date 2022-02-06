package tv.anilibria.module.data.network.datasource.storage

import android.content.SharedPreferences
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import org.json.JSONArray
import org.json.JSONObject
import tv.anilibria.module.data.network.DataPreferences
import tv.anilibria.module.data.network.datasource.holders.YearsHolder
import tv.anilibria.module.data.network.entity.app.release.YearItem
import javax.inject.Inject

/**
 * Created by radiationx on 17.02.18.
 */
class YearsStorage @Inject constructor(
        @DataPreferences private val sharedPreferences: SharedPreferences
) : YearsHolder {

    companion object {
        private const val LOCAL_YEARS_KEY = "data.local_years"
    }

    private val localYears = mutableListOf<YearItem>()
    private val localYearsRelay = BehaviorRelay.createDefault(localYears)

    init {
        loadAll()
    }

    override fun observeYears(): Observable<MutableList<YearItem>> = localYearsRelay

    override fun saveYears(years: List<YearItem>) {
        localYears.clear()
        localYears.addAll(years)
        saveAll()
        localYearsRelay.accept(localYears)
    }

    override fun getYears(): List<YearItem> = localYears

    private fun saveAll() {
        val jsonYears = JSONArray()
        localYears.forEach {
            jsonYears.put(JSONObject().apply {
                put("title", it.title)
                put("value", it.value)
            })
        }
        sharedPreferences
                .edit()
                .putString(LOCAL_YEARS_KEY, jsonYears.toString())
                .apply()
    }

    private fun loadAll() {
        val savedYears = sharedPreferences.getString(LOCAL_YEARS_KEY, null)
        savedYears?.let {
            val jsonYears = JSONArray(it)
            (0 until jsonYears.length()).forEach { index ->
                jsonYears.getJSONObject(index).let {
                    localYears.add(YearItem().apply {
                        title = it.getString("title")
                        value = it.getString("value")
                    })
                }
            }
        }
        localYearsRelay.accept(localYears)
    }
}