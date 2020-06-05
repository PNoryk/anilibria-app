package anilibria.tv.storage.impl

import anilibria.tv.domain.entity.menu.LinkMenu
import anilibria.tv.storage.common.KeyValueStorage
import anilibria.tv.storage.impl.converter.LinkMenuConverter
import com.google.gson.Gson
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Maybe
import org.junit.Test

class LinkMenuStorageDataSourceTest {

    private val KEY = "local_menu"
    private val gson = Gson()
    private val converter = LinkMenuConverter()
    private val keyValueStorage = mockk<KeyValueStorage>()
    private val dataSource = LinkMenuStorageDataSourceImpl(keyValueStorage, gson, converter)

    private val items = listOf(
        LinkMenu(
            title = "Группа VK",
            absoluteLink = "https://vk.com/anilibria",
            sitePagePath = null,
            icon = LinkMenu.IC_VK
        ),
        LinkMenu(
            title = "Канал YouTube",
            absoluteLink = "https://youtube.com/channel/UCuF8ghQWaa7K-28llm-K3Zg",
            sitePagePath = "/channel/UCuF8ghQWaa7K-28llm-K3Zg",
            icon = LinkMenu.IC_YOUTUBE
        )
    )
    private val json = """[{"title":"Группа VK","absoluteLink":"https://vk.com/anilibria","icon":"vk"},{"title":"Канал YouTube","absoluteLink":"https://youtube.com/channel/UCuF8ghQWaa7K-28llm-K3Zg","sitePagePath":"/channel/UCuF8ghQWaa7K-28llm-K3Zg","icon":"yotube"}]"""

    @Test
    fun `get when no data EXPECT empty list`() {
        every { keyValueStorage.getValue(KEY) } returns Maybe.fromCallable { null }

        dataSource.getList().test().assertValue(emptyList())

        verify { keyValueStorage.getValue(KEY) }
        confirmVerified(keyValueStorage)
    }

    @Test
    fun `put items EXPECT success`() {
        every { keyValueStorage.putValue(KEY, any()) } returns Completable.complete()

        dataSource.putList(items).test().assertComplete()

        verify { keyValueStorage.putValue(KEY, json) }
        confirmVerified(keyValueStorage)
    }

    @Test
    fun `get saved EXPECT not empty list`() {
        every { keyValueStorage.getValue(KEY) } returns Maybe.fromCallable { json }

        dataSource.getList().test().assertValue(items)

        verify { keyValueStorage.getValue(KEY) }
        confirmVerified(keyValueStorage)
    }

}