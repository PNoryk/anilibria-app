package anilibria.tv.db

import anilibria.tv.domain.entity.common.keys.ScheduleKey
import anilibria.tv.domain.entity.relative.ScheduleDayRelative
import io.reactivex.Completable
import io.reactivex.Single

interface ScheduleDbDataSource {
    fun getList(): Single<List<ScheduleDayRelative>>
    fun getSome(keys: List<ScheduleKey>): Single<List<ScheduleDayRelative>>
    fun getOne(key: ScheduleKey): Single<ScheduleDayRelative>
    fun insert(items: List<ScheduleDayRelative>): Completable
    fun remove(keys: List<ScheduleKey>): Completable
    fun clear(): Completable
}