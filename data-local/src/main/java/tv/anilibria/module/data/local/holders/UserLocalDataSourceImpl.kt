package tv.anilibria.module.data.local.holders

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import tv.anilibria.module.data.local.DataWrapper
import tv.anilibria.module.data.local.ObservableData
import tv.anilibria.module.domain.entity.other.User

class UserLocalDataSourceImpl : UserLocalDataSource {

    private val observableData = ObservableData<User>()

    override fun observe(): Observable<DataWrapper<User>> = observableData.observe()

    override fun get(): Single<DataWrapper<User>> = observableData.get()

    override fun put(data: User): Completable = observableData.put(DataWrapper(data))

    override fun delete(): Completable = observableData.put(DataWrapper(null))
}