package ru.radiationx.data.datasource.remote.address

import android.util.Log
import com.jakewharton.rxrelay2.PublishRelay
import javax.inject.Inject

@Deprecated("old data")
class ApiConfigChanger @Inject constructor(
) {
    private val relay = PublishRelay.create<Unit>()
    fun observeConfigChanges() = relay.hide()
    fun onChange() {
        Log.d("bobobo", "ApiConfigChanger onChange")
        relay.accept(Unit)
    }

}