package ru.radiationx.data.extensions

import io.reactivex.Single
import ru.radiationx.data.entity.common.DataWrapper

@Deprecated("old data")
fun <T> Single<DataWrapper<T>>.nullOnError() =
    this.onErrorReturn { DataWrapper(null) }

@Deprecated("old data")
fun <T> T.toWrapper() = DataWrapper(this)