package tv.anilibria.module.data.network.datasource.storage

import android.content.SharedPreferences
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import tv.anilibria.module.data.network.DataPreferences
import tv.anilibria.module.data.network.datasource.holders.AuthHolder
import java.util.*
import javax.inject.Inject

/**
 * Created by radiationx on 30.12.17.
 */
class AuthStorage @Inject constructor(
    @DataPreferences private val sharedPreferences: SharedPreferences
) : AuthHolder {

    companion object {
        private const val KEY_DEVICE_UID = "device_uid"
    }

    private val vkAuthRelay = PublishRelay.create<Boolean>()

    override fun observeVkAuthChange(): Observable<Boolean> = vkAuthRelay.hide()

    override fun changeVkAuth(value: Boolean) {
        vkAuthRelay.accept(value)
    }

    override fun getDeviceId(): String {
        var uid = sharedPreferences.getString(KEY_DEVICE_UID, null)

        if (uid == null) {
            uid = UUID.randomUUID()?.toString() ?: ""
            sharedPreferences.edit().putString(KEY_DEVICE_UID, uid).apply()
        }

        return uid
    }
}