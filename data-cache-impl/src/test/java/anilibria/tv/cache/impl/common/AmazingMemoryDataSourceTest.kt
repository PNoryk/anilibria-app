package anilibria.tv.cache.impl.common

import anilibria.tv.cache.impl.common.amazing.AmazingMemoryDataSource
import anilibria.tv.domain.entity.common.MemoryKey
import org.junit.Test

class AmazingMemoryDataSourceTest {

    private data class TestMemoryKey(val col1: Int?, val col2: String?) : MemoryKey(arrayOf(col1, col2))

    private val dataSource = AmazingMemoryDataSource<TestMemoryKey, String>()

    private fun String.toStringKey() = TestMemoryKey(null, this)
    private fun String.toStringKeyValue() = Pair(TestMemoryKey(null, this), this)
    private fun List<String>.toStringKeys() = map { it.toStringKey() }
    private fun List<String>.toStringKeyValues() = map { it.toStringKeyValue() }

    private fun Int.toIntKey() = TestMemoryKey(this, null)
    private fun Int.toIntKeyValue() = Pair(TestMemoryKey(this, null), this)
    private fun List<Int>.toIntKeys() = map { it.toIntKey() }
    private fun List<Int>.toIntKeyValues() = map { it.toIntKeyValue() }

    @Test
    fun `first fetch EXPECT empty list`() {
        val phantomKey = "phantom".toStringKey()

        dataSource.observeList().test().assertValue(emptyList())
        dataSource.observeSome(listOf(phantomKey)).test().assertValue(emptyList())
        dataSource.observeOne(phantomKey).test().assertEmpty()

        dataSource.getList().test().assertValue(emptyList())
        dataSource.getSome(listOf(phantomKey)).test().assertValue(emptyList())
        dataSource.getOne(phantomKey).test().assertError(NoSuchElementException::class.java)
    }

    @Test
    fun `insert items EXPECT success and notify`() {
        val insertItems = listOf("key1", "key2")
        val insertItem = insertItems[0]
        val insertKey = insertItem.toStringKey()
        val insertKeys = insertItems.toStringKeys()
        val insertKeyValues = insertItems.toStringKeyValues()

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
        val insertKey = insertItem.toStringKey()
        val insertKeys = insertItems.toStringKeys()
        val insertKeyValues = insertItems.toStringKeyValues()

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
        dataSource.removeList(deleteItems.toStringKeys()).test().assertComplete()

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
        val insertKey = insertItem.toStringKey()
        val insertKeys = insertItems.toStringKeys()
        val insertKeyValues = insertItems.toStringKeyValues()

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
        dataSource.getOne(insertKey).test().assertError(NoSuchElementException::class.java)
    }

    @Test
    fun `check column indexing`() {
        val records = listOf(
            createRecord(10, "may"),
            createRecord(10, "june"),
            createRecord(10, "july"),
            createRecord(10, "august"),

            createRecord(20, "may"),
            createRecord(30, "june"),
            createRecord(40, "july"),
            createRecord(50, "august"),

            createRecord(60, "june"),
            createRecord(70, "june"),
            createRecord(80, "june"),
            createRecord(90, "june")
        )

        val values = records.map { it.second }

        val dataObserver = dataSource.observeSome(records.map { it.first }).test()

        dataSource.insert(records).test().assertComplete()
        dataObserver.assertValueAt(1, values)
        dataSource.observeSome(records.map { it.first }).test().assertValue(values)

        dataSource.getList().test().assertValue(values)
        dataSource.getOne(TestMemoryKey(100, "june")).test().assertError(NoSuchElementException::class.java)
        dataSource.getSome(listOf(TestMemoryKey(100, "june"))).test().assertValue(emptyList())

        dataSource.getSome(
            listOf(
                TestMemoryKey(10, "may"),
                TestMemoryKey(90, "june"),
                TestMemoryKey(50, "august"),
                TestMemoryKey(10, "july")
            )
        ).test().assertValue(
            listOf(
                getRecordValue(10, "may"),
                getRecordValue(90, "june"),
                getRecordValue(50, "august"),
                getRecordValue(10, "july")
            )
        )

        dataSource.getOne(TestMemoryKey(null, "may")).test().assertValue(getRecordValue(10, "may"))
        dataSource.getSome(listOf(TestMemoryKey(null, "may"))).test().assertValue(
            listOf(
                getRecordValue(10, "may"),
                getRecordValue(20, "may")
            )
        )

        dataSource.getOne(TestMemoryKey(10, null)).test().assertValue(getRecordValue(10, "may"))
        dataSource.getSome(listOf(TestMemoryKey(10, null))).test().assertValue(
            listOf(
                getRecordValue(10, "may"),
                getRecordValue(10, "june"),
                getRecordValue(10, "july"),
                getRecordValue(10, "august")
            )
        )

        dataSource.getOne(TestMemoryKey(null, null)).test().assertError(NoSuchElementException::class.java)

    }

    private fun getRecordValue(col1: Int?, col2: String?) = "record_$col1:$col2"

    private fun createRecord(col1: Int?, col2: String?) = Pair(
        TestMemoryKey(col1, col2),
        getRecordValue(col1, col2)
    )
}