package tv.anilibria.module.data.local.holders

import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import tv.anilibria.module.data.local.entity.SocialAuthServiceLocal
import tv.anilibria.module.data.local.mappers.toDomain
import tv.anilibria.module.data.local.mappers.toLocal
import tv.anilibria.module.domain.entity.auth.SocialAuthService
import tv.anilibria.plugin.data.storage.DataWrapper
import tv.anilibria.plugin.data.storage.MoshiPreferencesPersistentDataStore
import tv.anilibria.plugin.data.storage.ObservableData

class SocialAuthLocalDataSourceImpl(
    private val preferences: SharedPreferences,
    private val moshi: Moshi
) {

    private val adapter by lazy {
        val type = Types.newParameterizedType(List::class.java, SocialAuthServiceLocal::class.java)
        moshi.adapter<List<SocialAuthServiceLocal>>(type)
    }

    private val persistableData =
        MoshiPreferencesPersistentDataStore<List<SocialAuthServiceLocal>, List<SocialAuthService>>(
            key = "refactor.social_auth_services",
            adapter = adapter,
            preferences = preferences,
            read = { data -> data?.map { it.toDomain() } },
            write = { data -> data?.map { it.toLocal() } }
        )

    private val observableData = ObservableData(persistableData)

    fun observe(): Observable<List<SocialAuthService>> = observableData
        .observe()
        .map { it.data.orEmpty() }

    fun get(): Single<List<SocialAuthService>> = observableData
        .get()
        .map { it.data.orEmpty() }

    fun put(data: List<SocialAuthService>): Completable = observableData
        .put(DataWrapper(data))
}