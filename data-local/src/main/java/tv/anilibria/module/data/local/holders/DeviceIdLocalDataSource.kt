package tv.anilibria.module.data.local.holders

import tv.anilibria.plugin.data.storage.DataStorage
import java.util.*

class DeviceIdLocalDataSource(
    private val storage: DataStorage
) {

    companion object {
        private const val KEY_DEVICE_UID = "device_uid"
    }

    suspend fun get(): String {
        var uid = storage.getString(KEY_DEVICE_UID)

        if (uid == null) {
            uid = UUID.randomUUID().toString()
            storage.setString(KEY_DEVICE_UID, uid)
        }

        return uid
    }
}