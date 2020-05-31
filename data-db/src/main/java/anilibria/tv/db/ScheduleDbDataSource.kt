package anilibria.tv.db

import anilibria.tv.domain.entity.relative.ScheduleDayRelative
import io.reactivex.Completable
import io.reactivex.Single

interface ScheduleDbDataSource {
    fun getListAll(): Single<List<ScheduleDayRelative>>
    fun getList(ids: List<Int>): Single<List<ScheduleDayRelative>>
    fun getOne(dayId: Int): Single<ScheduleDayRelative>
    fun insert(items: List<ScheduleDayRelative>): Completable
    fun removeList(ids: List<Int>): Completable
    fun deleteAll(): Completable
}