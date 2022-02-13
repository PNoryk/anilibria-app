package tv.anilibria.plugin.data.storage

sealed class StorageKey<T>(val name: String) {
    class IntKey(name: String) : StorageKey<Int>(name)
    class BooleanKey(name: String) : StorageKey<Boolean>(name)
    class FloatKey(name: String) : StorageKey<Float>(name)
    class StringKey(name: String) : StorageKey<String>(name)
    class LongKey(name: String) : StorageKey<Long>(name)
}

fun storageIntKey(name: String) = StorageKey.IntKey(name)
fun storageBooleanKey(name: String) = StorageKey.BooleanKey(name)
fun storageFloatKey(name: String) = StorageKey.FloatKey(name)
fun storageStringKey(name: String) = StorageKey.StringKey(name)
fun storageLongKey(name: String) = StorageKey.LongKey(name)
