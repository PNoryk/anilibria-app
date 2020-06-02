package anilibria.tv.storage

import io.reactivex.Single

interface DeviceIdStorageDataSource {

    fun getDeviceId(): Single<String>
}