package tv.anilibria.module.data.network.datasource.storage

import android.content.SharedPreferences
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import org.json.JSONArray
import org.json.JSONObject
import tv.anilibria.module.data.network.DataPreferences
import tv.anilibria.module.data.network.datasource.holders.MenuHolder
import tv.anilibria.module.data.network.entity.app.other.DataIcons
import tv.anilibria.module.data.network.entity.app.other.LinkMenuItemResponse
import ru.radiationx.shared.ktx.android.nullString
import javax.inject.Inject

class MenuStorage @Inject constructor(
    @DataPreferences private val sharedPreferences: SharedPreferences
) : MenuHolder {

    companion object {
        private const val LOCAL_MENU_KEY = "data.local_menu"
    }

    private val localMenu = mutableListOf(
        LinkMenuItemResponse(
            "Группа VK",
            absoluteLink = "https://vk.com/anilibria",
            icon = DataIcons.VK
        ),
        LinkMenuItemResponse(
            "Канал YouTube",
            absoluteLink = "https://youtube.com/channel/UCuF8ghQWaa7K-28llm-K3Zg",
            icon = DataIcons.YOUTUBE
        ),
        LinkMenuItemResponse(
            "Patreon",
            absoluteLink = "https://patreon.com/anilibria",
            icon = DataIcons.PATREON
        ),
        LinkMenuItemResponse(
            "Канал Telegram",
            absoluteLink = "https://t.me/anilibria_tv",
            icon = DataIcons.TELEGRAM
        ),
        LinkMenuItemResponse(
            "Чат Discord",
            absoluteLink = "https://discord.gg/Kdr5sNw",
            icon = DataIcons.DISCORD
        ),
        LinkMenuItemResponse(
            "Сайт AniLibria",
            absoluteLink = "https://www.anilibria.tv/",
            icon = DataIcons.ANILIBRIA
        )
    )
    private val localMenuRelay = BehaviorRelay.createDefault<List<LinkMenuItemResponse>>(localMenu)

    init {
        loadAll()
    }

    override fun observe(): Observable<List<LinkMenuItemResponse>> = localMenuRelay.hide()

    override fun save(items: List<LinkMenuItemResponse>) {
        localMenu.clear()
        localMenu.addAll(items)
        saveAll()
        localMenuRelay.accept(localMenu)
    }

    override fun get(): List<LinkMenuItemResponse> = localMenu

    private fun saveAll() {
        val jsonMenu = JSONArray()
        localMenu.forEach {
            jsonMenu.put(JSONObject().apply {
                put("title", it.title)
                put("absoluteLink", it.absoluteLink)
                put("sitePagePath", it.sitePagePath)
                put("icon", it.icon)
            })
        }
        sharedPreferences
            .edit()
            .putString(LOCAL_MENU_KEY, jsonMenu.toString())
            .apply()
    }

    private fun loadAll() {
        sharedPreferences.getString(LOCAL_MENU_KEY, null)?.also { savedMenu ->
            val jsonMenu = JSONArray(savedMenu)
            localMenu.clear()
            (0 until jsonMenu.length()).forEach { index ->
                jsonMenu.getJSONObject(index).also {
                    localMenu.add(
                        LinkMenuItemResponse(
                            it.getString("title"),
                            it.nullString("absoluteLink"),
                            it.nullString("sitePagePath"),
                            it.nullString("icon")
                        )
                    )
                }
            }
        }
        localMenuRelay.accept(localMenu)
    }
}