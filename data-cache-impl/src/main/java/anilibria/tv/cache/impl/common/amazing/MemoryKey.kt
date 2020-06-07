package anilibria.tv.cache.impl.common.amazing

open class MemoryKey(val columns: Array<Any?>) {

    fun hasAnyEqualKeys(other: MemoryKey): Boolean {
        val otherColumns = other.columns
        for (index in columns.indices) {
            val column = columns[index]
            val otherColumn = otherColumns[index]

            if (column != null && column != otherColumns[index]) {
                return false
            }
        }
        return true
    }


}