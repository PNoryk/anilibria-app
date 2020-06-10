package anilibria.tv.cache.impl.memory

import org.junit.Test

class GenreMemoryDataSourceTest {

    private val dataSource = GenreMemoryDataSource()

    @Test
    fun `first fetch EXPECT empty list`() {
        dataSource.observeList().test().assertValue(emptyList())
        dataSource.getList().test().assertValue(emptyList())
    }

    @Test
    fun `insert items EXPECT success and notify`() {
        val insertItems = listOf("genre1", "genre2")

        dataSource.insert(insertItems).test().assertComplete()

        dataSource.observeList().test().assertValue(insertItems)
        dataSource.getList().test().assertValue(insertItems)
    }

    @Test
    fun `delete items EXPECT success and notify`() {
        val insertItems = listOf("genre1", "genre2")
        val deleteItems = listOf("genre2")
        val afterDeleteItems = listOf("genre1")
        val dataObserver = dataSource.observeList().test()

        // action
        dataSource.insert(insertItems).test().assertComplete()

        // verify
        dataObserver.assertValueAt(1, insertItems)
        dataSource.observeList().test().assertValue(insertItems)
        dataSource.getList().test().assertValue(insertItems)

        // action
        dataSource.remove(deleteItems).test().assertComplete()

        // verify
        dataObserver.assertValueAt(2, afterDeleteItems)
        dataSource.observeList().test().assertValue(afterDeleteItems)
        dataSource.getList().test().assertValue(afterDeleteItems)
    }

    @Test
    fun `delete all items EXPECT success and notify`() {
        val insertItems = listOf("genre1", "genre2")
        val dataObserver = dataSource.observeList().test()

        // action
        dataSource.insert(insertItems).test().assertComplete()

        // verify
        dataObserver.assertValueAt(1, insertItems)
        dataSource.observeList().test().assertValue(insertItems)
        dataSource.getList().test().assertValue(insertItems)

        // action
        dataSource.clear().test().assertComplete()

        // verify
        dataObserver.assertValueAt(2, emptyList())
        dataSource.observeList().test().assertValue(emptyList())
        dataSource.getList().test().assertValue(emptyList())
    }
}