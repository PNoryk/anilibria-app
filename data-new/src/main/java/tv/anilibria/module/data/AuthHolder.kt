package tv.anilibria.module.data


/**
 * Created by radiationx on 30.12.17.
 */
interface AuthHolder {
    suspend fun getDeviceId(): String
}