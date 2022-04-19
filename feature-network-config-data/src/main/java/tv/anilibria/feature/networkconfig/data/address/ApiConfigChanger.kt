package tv.anilibria.feature.networkconfig.data.address

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import toothpick.InjectConstructor

@InjectConstructor
class ApiConfigChanger {

    private val relay = MutableSharedFlow<Unit>()

    fun observeConfigChanges(): Flow<Unit> = relay

    suspend fun onChange() {
        Log.d("bobobo", "ApiConfigChanger onChange")
        relay.emit(Unit)
    }
}