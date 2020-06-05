package anilibria.tv.storage.impl

import anilibria.tv.storage.common.KeyValueStorage
import com.google.gson.Gson
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Maybe
import org.junit.Test

class GenreStorageDataSourceTest {

    private val KEY = "local_genres"
    private val gson = Gson()
    private val keyValueStorage = mockk<KeyValueStorage>()
    private val dataSource = GenreStorageDataSourceImpl(keyValueStorage, gson)

    private val items = listOf("genre1", "genre2")
    private val json = """["genre1","genre2"]"""

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