package tv.anilibria.feature.auth.data.local

import tv.anilibria.feature.auth.data.di.AuthStorageQualifier
import tv.anilibria.plugin.data.storage.DataStorage
import tv.anilibria.plugin.data.storage.storageStringKey
import java.util.*

class DeviceIdLocalDataSource(
    @AuthStorageQualifier private val storage: DataStorage
) {

    companion object {
        private val KEY_DEVICE_UID = storageStringKey("device_uid")
    }

    suspend fun get(): String {
        var uid = storage.get(KEY_DEVICE_UID)

        if (uid == null) {
            uid = UUID.randomUUID().toString()
            storage.set(KEY_DEVICE_UID, uid)
        }

        return uid
    }
}