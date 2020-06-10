package anilibria.tv.domain.entity.common

open class MemoryKey(val columns: Array<Any?>) {

    fun hasAnyEqualKeys(other: MemoryKey): Boolean {
        val otherColumns = other.columns
        for (index in columns.indices) {
            val column = columns[index]
            if (column != null && column != otherColumns[index]) {
                return false
            }
        }
        return true
    }
}