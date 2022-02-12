package tv.anilibria.feature.networkconfig.data

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import tv.anilibria.feature.networkconfig.data.domain.ApiAddress
import tv.anilibria.plugin.data.storage.DataStorage
import tv.anilibria.plugin.data.storage.DataWrapper
import tv.anilibria.plugin.data.storage.ObservableData

class ConfigLocalDataStorage(
    private val storage: DataStorage,
) {

    private val observableData = ObservableData<List<ApiAddress>>()

    private val observableActive = ObservableData<String>()

    fun observe(): Observable<DataWrapper<List<ApiAddress>>> = observableData.observe()

    fun set(data: List<ApiAddress>): Completable = observableData.put(DataWrapper(data))

    fun get(): Single<DataWrapper<List<ApiAddress>>> = observableData.get()

    fun observeActive(): Observable<DataWrapper<String>> = observableActive.observe()

    fun setActive(tag: String): Completable = observableActive.put(DataWrapper(tag))

    fun getActive(): Single<DataWrapper<String>> = observableActive.get()
}