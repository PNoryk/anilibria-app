package tv.anilibria.module.data

import tv.anilibria.module.domain.entity.AuthState

interface AuthStateHolder {

    suspend fun skip()
    suspend fun get(): AuthState
}