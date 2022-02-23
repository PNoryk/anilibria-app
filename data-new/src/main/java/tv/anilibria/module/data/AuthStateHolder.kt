package tv.anilibria.module.data

import kotlinx.coroutines.flow.Flow
import tv.anilibria.module.domain.entity.AuthState

interface AuthStateHolder {

    fun observe(): Flow<AuthState>
    suspend fun get(): AuthState
    suspend fun skip()
}