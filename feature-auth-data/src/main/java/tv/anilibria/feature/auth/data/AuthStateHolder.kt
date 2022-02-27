package tv.anilibria.feature.auth.data

import kotlinx.coroutines.flow.Flow
import tv.anilibria.feature.auth.data.domain.AuthState

interface AuthStateHolder {

    fun observe(): Flow<AuthState>
    suspend fun get(): AuthState
    suspend fun skip()
}