package anilibria.tv.storage.impl

import anilibria.tv.storage.DeviceIdStorageDataSource
import anilibria.tv.storage.common.KeyValueStorage
import io.reactivex.Single
import toothpick.InjectConstructor
import java.util.*

@InjectConstructor
class DeviceIdStorageDataSourceImpl(
    private val keyValueStorage: KeyValueStorage
) : DeviceIdStorageDataSource {

    companion object {
        private const val KEY = "device_uid"
    }

    override fun getDeviceId(): Single<String> = keyValueStorage
        .getValue(KEY)
        .switchIfEmpty(generate())

    private fun generate(): Single<String> = Single
        .fromCallable { UUID.randomUUID()?.toString() ?: "" }
        .flatMapCompletable { keyValueStorage.putValue(KEY, it) }
        .andThen(keyValueStorage.getValue(KEY))
        .toSingle()
}