package anilibria.tv.storage.impl

import anilibria.tv.domain.entity.menu.LinkMenu
import anilibria.tv.storage.LinkMenuStorageDataSource
import anilibria.tv.storage.common.KeyValueStorage
import anilibria.tv.storage.impl.converter.LinkMenuConverter
import anilibria.tv.storage.impl.entity.LinkMenuStorage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Completable
import io.reactivex.Single
import toothpick.InjectConstructor

@InjectConstructor
class LinkMenuStorageDataSourceImpl(
    private val keyValueStorage: KeyValueStorage,
    private val gson: Gson,
    private val converter: LinkMenuConverter
) : LinkMenuStorageDataSource {

    companion object {
        private const val KEY = "local_menu"

        private val dataType = object : TypeToken<List<LinkMenuStorage>>() {}.type

        //todo перенести дефолтные значения в репозиторий или куда-нибудь повыше
        private val defaultMenu = listOf(
            LinkMenu(
                title = "Группа VK",
                absoluteLink = "https://vk.com/anilibria",
                icon = LinkMenu.IC_VK,
                sitePagePath = null
            ),
            LinkMenu(
                title = "Канал YouTube",
                absoluteLink = "https://youtube.com/channel/UCuF8ghQWaa7K-28llm-K3Zg",
                icon = LinkMenu.IC_YOUTUBE,
                sitePagePath = null
            ),
            LinkMenu(
                title = "Patreon",
                absoluteLink = "https://patreon.com/anilibria",
                icon = LinkMenu.IC_PATREON,
                sitePagePath = null
            ),
            LinkMenu(
                title = "Канал Telegram",
                absoluteLink = "https://t.me/anilibria_tv",
                icon = LinkMenu.IC_TELEGRAM,
                sitePagePath = null
            ),
            LinkMenu(
                title = "Чат Discord",
                absoluteLink = "https://discord.gg/Kdr5sNw",
                icon = LinkMenu.IC_DISCORD,
                sitePagePath = null
            ),
            LinkMenu(
                title = "Сайт AniLibria",
                absoluteLink = "https://www.anilibria.tv/",
                icon = LinkMenu.IC_ANILIBRIA,
                sitePagePath = null
            )
        )
    }

    override fun getList(): Single<List<LinkMenu>> = keyValueStorage
        .getValue(KEY)
        .map { gson.fromJson<List<LinkMenuStorage>>(it, dataType) }
        .map { converter.toDomain(it) }
        .switchIfEmpty(Single.fromCallable { emptyList<LinkMenu>() })

    override fun putList(items: List<LinkMenu>): Completable = Single
        .fromCallable { converter.toStorage(items) }
        .map { gson.toJson(it, dataType) }
        .flatMapCompletable { keyValueStorage.putValue(KEY, it) }

    override fun clear(): Completable = keyValueStorage.delete(KEY)
}