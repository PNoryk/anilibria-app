package tv.anilibria.module.data.local.holders

import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import tv.anilibria.module.data.local.DataWrapper
import tv.anilibria.module.data.local.MoshiPreferencesPersistableData
import tv.anilibria.module.data.local.ObservableData

class YearsLocalDataSourceImpl(
    private val preferences: SharedPreferences,
    private val moshi: Moshi
) : YearsLocalDataSource {

    private val adapter by lazy {
        val type = Types.newParameterizedType(List::class.java, String::class.java)
        moshi.adapter<List<String>>(type)
    }

    private val persistableData = MoshiPreferencesPersistableData<List<String>, List<String>>(
        key = "refactor.years",
        adapter = adapter,
        preferences = preferences,
        read = { it },
        write = { it }
    )

    private val observableData = ObservableData(persistableData)

    override fun observe(): Observable<List<String>> = observableData
        .observe()
        .map { it.data.orEmpty() }

    override fun get(): Single<List<String>> = observableData
        .get()
        .map { it.data.orEmpty() }

    override fun put(data: List<String>): Completable = observableData
        .put(DataWrapper(data))
}