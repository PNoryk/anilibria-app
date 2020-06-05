package anilibria.tv.storage.impl

import anilibria.tv.storage.YearStorageDataSource
import anilibria.tv.storage.common.KeyValueStorage
import com.google.gson.Gson
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Maybe
import org.junit.Test

class YearStorageDataSourceTest {

    private val KEY = "local_years"
    private val gson = Gson()
    private val keyValueStorage = mockk<KeyValueStorage>()
    private val dataSource = YearStorageDataSourceImpl(keyValueStorage, gson)

    private val items = listOf("year1", "year2")
    private val json = """["year1","year2"]"""

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