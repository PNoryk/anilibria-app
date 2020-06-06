package anilibria.tv.cache.impl.common

import anilibria.tv.cache.impl.common.amazing.AmazingMemoryDataSource
import anilibria.tv.cache.impl.common.amazing.MemoryKey
import org.junit.Test

class AmazingMemoryDataSourceTest {

    private val dataSource = AmazingMemoryDataSource<String>()

    private data class StringMemoryKey(val key: String) : MemoryKey()

    private fun String.toKey() = StringMemoryKey(this)
    private fun String.toKeyValue() = Pair(StringMemoryKey(this), this)
    private fun List<String>.toKeys() = map { it.toKey() }
    private fun List<String>.toKeyValues() = map { it.toKeyValue() }

    @Test
    fun `first fetch EXPECT empty list`() {
        val phantomKey = "phantom".toKey()

        dataSource.observeList().test().assertValue(emptyList())
        dataSource.observeSome(listOf(phantomKey)).test().assertValue(emptyList())
        dataSource.observeOne(phantomKey).test().assertEmpty()

        dataSource.getList().test().assertValue(emptyList())
        dataSource.getSome(listOf(phantomKey)).test().assertValue(emptyList())
        dataSource.getOne(phantomKey).test().assertComplete()
    }

    @Test
    fun `insert items EXPECT success and notify`() {
        val insertItems = listOf("key1", "key2")
        val insertItem = insertItems[0]
        val insertKey = insertItem.toKey()
        val insertKeys = insertItems.toKeys()
        val insertKeyValues = insertItems.toKeyValues()

        val listObserver = dataSource.observeList().test()
        val someObserver = dataSource.observeSome(insertKeys).test()
        val oneObserver = dataSource.observeOne(insertKey).test()

        dataSource.insert(insertKeyValues).test().assertComplete()

        listObserver.assertValueAt(1, insertItems)
        someObserver.assertValueAt(1, insertItems)
        oneObserver.assertValue(insertItem)

        dataSource.observeList().test().assertValue(insertItems)
        dataSource.observeSome(insertKeys).test().assertValue(insertItems)
        dataSource.observeOne(insertKey).test().assertValue(insertItem)

        dataSource.getList().test().assertValue(insertItems)
        dataSource.getSome(insertKeys).test().assertValue(insertItems)
        dataSource.getOne(insertKey).test().assertValue(insertItem)
    }

    @Test
    fun `delete items EXPECT success and notify`() {
        val insertItems = listOf("key1", "key2")
        val deleteItems = listOf(insertItems[1])
        val afterDeleteItems = listOf(insertItems[0])

        val insertItem = insertItems[0]
        val insertKey = insertItem.toKey()
        val insertKeys = insertItems.toKeys()
        val insertKeyValues = insertItems.toKeyValues()

        val listObserver = dataSource.observeList().test()
        val someObserver = dataSource.observeSome(insertKeys).test()
        val oneObserver = dataSource.observeOne(insertKey).test()


        // Insert
        dataSource.insert(insertKeyValues).test().assertComplete()

        // Verify insert
        listObserver.assertValueAt(1, insertItems)
        someObserver.assertValueAt(1, insertItems)
        oneObserver.assertValue(insertItem)

        dataSource.observeList().test().assertValue(insertItems)
        dataSource.observeSome(insertKeys).test().assertValue(insertItems)
        dataSource.observeOne(insertKey).test().assertValue(insertItem)

        dataSource.getList().test().assertValue(insertItems)
        dataSource.getSome(insertKeys).test().assertValue(insertItems)
        dataSource.getOne(insertKey).test().assertValue(insertItem)


        // Remove
        dataSource.removeList(deleteItems.toKeys()).test().assertComplete()

        // Verify remove
        listObserver.assertValueAt(2, afterDeleteItems)
        someObserver.assertValueAt(2, afterDeleteItems)
        oneObserver.assertValueAt(1, insertItem)

        dataSource.observeList().test().assertValue(afterDeleteItems)
        dataSource.observeSome(insertKeys).test().assertValue(afterDeleteItems)
        dataSource.observeOne(insertKey).test().assertValue(insertItem)

        dataSource.getList().test().assertValue(afterDeleteItems)
        dataSource.getSome(insertKeys).test().assertValue(afterDeleteItems)
        dataSource.getOne(insertKey).test().assertValue(insertItem)
    }

    @Test
    fun `delete all items EXPECT success and notify`() {
        val insertItems = listOf("key1", "key2")
        val afterClearItems = emptyList<String>()

        val insertItem = insertItems[0]
        val insertKey = insertItem.toKey()
        val insertKeys = insertItems.toKeys()
        val insertKeyValues = insertItems.toKeyValues()

        val listObserver = dataSource.observeList().test()
        val someObserver = dataSource.observeSome(insertKeys).test()
        val oneObserver = dataSource.observeOne(insertKey).test()


        // Insert
        dataSource.insert(insertKeyValues).test().assertComplete()

        // Verify insert
        listObserver.assertValueAt(1, insertItems)
        someObserver.assertValueAt(1, insertItems)
        oneObserver.assertValue(insertItem)

        dataSource.observeList().test().assertValue(insertItems)
        dataSource.observeSome(insertKeys).test().assertValue(insertItems)
        dataSource.observeOne(insertKey).test().assertValue(insertItem)

        dataSource.getList().test().assertValue(insertItems)
        dataSource.getSome(insertKeys).test().assertValue(insertItems)
        dataSource.getOne(insertKey).test().assertValue(insertItem)


        // Clear
        dataSource.clear().test().assertComplete()

        // Verify clear
        listObserver.assertValueAt(2, emptyList())
        someObserver.assertValueAt(2, emptyList())
        oneObserver.assertValue(insertItem)

        dataSource.observeList().test().assertValue(afterClearItems)
        dataSource.observeSome(insertKeys).test().assertValue(afterClearItems)
        dataSource.observeOne(insertKey).test().assertEmpty()

        dataSource.getList().test().assertValue(afterClearItems)
        dataSource.getSome(insertKeys).test().assertValue(afterClearItems)
        dataSource.getOne(insertKey).test().assertComplete()
    }
}