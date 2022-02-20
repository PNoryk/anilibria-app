package tv.anilibria.module.data

import kotlinx.coroutines.flow.Flow
import tv.anilibria.module.domain.entity.AuthState

// todo implement
interface AuthStateHolder {

    suspend fun skip()
    suspend fun get(): AuthState
    fun observe(): Flow<AuthState>
}