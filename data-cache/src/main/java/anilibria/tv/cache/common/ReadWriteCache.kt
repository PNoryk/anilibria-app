package anilibria.tv.cache.common

import anilibria.tv.domain.entity.common.MemoryKey

interface ReadWriteCache<K : MemoryKey, T> : ReadableCache<T>, WritableCache<K, T>