package anilibria.tv.cache.impl.memory

import org.junit.Test

class YearMemoryDataSourceTest {

    private val dataSource = YearMemoryDataSource()

    @Test
    fun `first fetch EXPECT empty list`() {
        dataSource.observeList().test().assertValue(emptyList())
        dataSource.getList().test().assertValue(emptyList())
    }

    @Test
    fun `insert items EXPECT success and notify`() {
        val insertItems = listOf("year1", "year2")

        dataSource.insert(insertItems).test().assertComplete()

        dataSource.observeList().test().assertValue(insertItems)
        dataSource.getList().test().assertValue(insertItems)
    }

    @Test
    fun `delete items EXPECT success and notify`() {
        val insertItems = listOf("year1", "year2")
        val deleteItems = listOf("year2")
        val afterDeleteItems = listOf("year1")
        val dataObserver = dataSource.observeList().test()

        // action
        dataSource.insert(insertItems).test().assertComplete()

        // verify
        dataObserver.assertValueAt(1, insertItems)
        dataSource.observeList().test().assertValue(insertItems)
        dataSource.getList().test().assertValue(insertItems)

        // action
        dataSource.removeList(deleteItems).test().assertComplete()

        // verify
        dataObserver.assertValueAt(2, afterDeleteItems)
        dataSource.observeList().test().assertValue(afterDeleteItems)
        dataSource.getList().test().assertValue(afterDeleteItems)
    }

    @Test
    fun `delete all items EXPECT success and notify`() {
        val insertItems = listOf("year1", "year2")
        val dataObserver = dataSource.observeList().test()

        // action
        dataSource.insert(insertItems).test().assertComplete()

        // verify
        dataObserver.assertValueAt(1, insertItems)
        dataSource.observeList().test().assertValue(insertItems)
        dataSource.getList().test().assertValue(insertItems)

        // action
        dataSource.deleteAll().test().assertComplete()

        // verify
        dataObserver.assertValueAt(2, emptyList())
        dataSource.observeList().test().assertValue(emptyList())
        dataSource.getList().test().assertValue(emptyList())
    }
}