package tv.anilibria.plugin.data.storage

sealed class StorageKey<T>(val name: String, val default: T) {
    class IntKey(name: String, default: Int) : StorageKey<Int>(name, default)
    class BooleanKey(name: String, default: Boolean) : StorageKey<Boolean>(name, default)
    class FloatKey(name: String, default: Float) : StorageKey<Float>(name, default)
    class StringKey(name: String, default: String? = null) : StorageKey<String?>(name, default)
    class LongKey(name: String, default: Long) : StorageKey<Long>(name, default)
}

fun storageIntKey(name: String, default: Int) = StorageKey.IntKey(name, default)
fun storageBooleanKey(name: String, default: Boolean) = StorageKey.BooleanKey(name, default)
fun storageFloatKey(name: String, default: Float) = StorageKey.FloatKey(name, default)
fun storageStringKey(name: String, default: String? = null) = StorageKey.StringKey(name, default)
fun storageLongKey(name: String, default: Long) = StorageKey.LongKey(name, default)
