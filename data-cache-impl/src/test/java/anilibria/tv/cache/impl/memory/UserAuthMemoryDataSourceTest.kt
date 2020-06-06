package anilibria.tv.cache.impl.memory

import anilibria.tv.domain.entity.auth.UserAuth
import anilibria.tv.domain.entity.menu.LinkMenu
import io.mockk.mockk
import org.junit.Test

class UserAuthMemoryDataSourceTest {

    private val dataSource = UserAuthMemoryDataSource()

    @Test
    fun `first fetch EXPECT empty data`() {
        dataSource.observeData().test().assertEmpty()
        dataSource.getData().test().assertComplete()
    }

    @Test
    fun `insert data EXPECT success and notify`() {
        val insertData = mockk<UserAuth>()
        val dataObserver = dataSource.observeData().test()

        // action
        dataSource.insert(insertData).test().assertComplete()

        // verify
        dataObserver.assertValue(insertData)
        dataSource.observeData().test().assertValue(insertData)
        dataSource.getData().test().assertValue(insertData)
    }

    @Test
    fun `delete data EXPECT success, empty data`() {
        val insertData = mockk<UserAuth>()
        val dataObserver = dataSource.observeData().test()

        // action
        dataSource.insert(insertData).test().assertComplete()

        // verify
        dataObserver.assertValue(insertData)
        dataSource.getData().test().assertValue(insertData)

        // action
        dataSource.delete().test().assertComplete()

        // verify
        dataObserver.assertValue(insertData)
        dataSource.observeData().test().assertEmpty()
        dataSource.getData().test().assertComplete()
    }
}