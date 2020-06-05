package anilibria.tv.storage.impl

import anilibria.tv.domain.entity.auth.SocialService
import anilibria.tv.domain.entity.menu.LinkMenu
import anilibria.tv.storage.common.KeyValueStorage
import anilibria.tv.storage.impl.converter.LinkMenuConverter
import anilibria.tv.storage.impl.converter.SocialServiceConverter
import com.google.gson.Gson
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Maybe
import org.junit.Test

class SocialServiceStorageDataSourceTest {

    private val KEY = "social_auth"
    private val gson = Gson()
    private val converter = SocialServiceConverter()
    private val keyValueStorage = mockk<KeyValueStorage>()
    private val dataSource = SocialServiceStorageDataSourceImpl(keyValueStorage, gson, converter)

    private val items = listOf(
        SocialService(
            key = "soc1",
            title = "amazing service 1",
            socialUrl = "amazing://service.1",
            resultPattern = "its amazing",
            errorUrlPattern = "not amazing"
        ),
        SocialService(
            key = "soc2",
            title = "regular service 2",
            socialUrl = "regular://service.2",
            resultPattern = "its regular",
            errorUrlPattern = "not regular"
        )
    )
    private val json =
        """[{"key":"soc1","title":"amazing service 1","socialUrl":"amazing://service.1","resultPattern":"its amazing","errorUrlPattern":"not amazing"},{"key":"soc2","title":"regular service 2","socialUrl":"regular://service.2","resultPattern":"its regular","errorUrlPattern":"not regular"}]"""

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