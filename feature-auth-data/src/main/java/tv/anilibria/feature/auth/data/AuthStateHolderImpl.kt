package tv.anilibria.feature.auth.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import tv.anilibria.feature.auth.data.di.AuthStorageQualifier
import tv.anilibria.feature.auth.data.domain.AuthState
import tv.anilibria.feature.content.data.network.CookieData
import tv.anilibria.feature.content.data.network.CookiesStorage
import tv.anilibria.plugin.data.storage.DataStorage
import tv.anilibria.plugin.data.storage.ObservableData
import tv.anilibria.plugin.data.storage.StorageDataHolder
import tv.anilibria.plugin.data.storage.storageBooleanKey

class AuthStateHolderImpl(
    private val cookiesStorage: CookiesStorage,
    @AuthStorageQualifier private val storage: DataStorage
) : AuthStateHolder {

    private companion object {
        private const val SESSION_COOKIE = "PHPSESSID"
    }

    private val skippedHolder = StorageDataHolder(
        storageBooleanKey("auth_skipped", false),
        storage
    )

    private val skippedData = ObservableData(skippedHolder)

    override fun observe(): Flow<AuthState> = cookiesStorage.observe()
        .combine(skippedData.observe()) { cookies, isSkipped ->
            getState(cookies, isSkipped)
        }
        .distinctUntilChanged()

    override suspend fun get(): AuthState {
        return getState(cookiesStorage.get(), skippedData.get())
    }

    override suspend fun skip() {
        skippedData.put(true)
    }

    private fun getState(cookies: List<CookieData>, isSkipped: Boolean): AuthState {
        val hasCookie = cookies.any { it.cookie.name() == SESSION_COOKIE }
        return when {
            hasCookie -> AuthState.AUTH
            !hasCookie && isSkipped -> AuthState.AUTH_SKIPPED
            else -> AuthState.NO_AUTH
        }
    }
}