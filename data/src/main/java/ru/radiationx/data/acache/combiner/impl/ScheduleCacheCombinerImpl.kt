package ru.radiationx.data.acache.combiner.impl

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import ru.radiationx.data.acache.ScheduleCache
import ru.radiationx.data.acache.combiner.ReleaseCacheCombiner
import ru.radiationx.data.acache.combiner.ScheduleCacheCombiner
import ru.radiationx.data.adomain.entity.relative.ScheduleDayRelative
import ru.radiationx.data.adomain.entity.release.Release
import ru.radiationx.data.adomain.entity.schedule.ScheduleDay
import toothpick.InjectConstructor

@InjectConstructor
class ScheduleCacheCombinerImpl(
    private val scheduleCache: ScheduleCache,
    private val releaseCache: ReleaseCacheCombiner
) : ScheduleCacheCombiner {

    private val combiner by lazy {
        BiFunction<List<ScheduleDayRelative>, List<Release>, List<ScheduleDay>> { relativeItems, releaseItems ->
            relativeItems.map { relative ->
                val releases = relative.releaseIds.mapNotNull { releaseId ->
                    releaseItems.firstOrNull { it.id == releaseId }
                }
                ScheduleDay(relative.dayId, releases)
            }
        }
    }

    override fun observeList(): Observable<List<ScheduleDay>> = Observable
        .combineLatest(
            scheduleCache.observeList(),
            releaseCache.observeList(),
            combiner
        )

    override fun fetchList(): Single<List<ScheduleDay>> = Single.zip(
        scheduleCache.fetchList(),
        releaseCache.fetchList(),
        combiner
    )

    override fun putList(items: List<ScheduleDay>): Completable {
        val putRelease = releaseCache.putList(items.map { it.items }.flatten())
        val putSchedule = scheduleCache.putList(items.map {
            val releaseIds = it.items.map { release -> release.id }
            ScheduleDayRelative(it.day, releaseIds)
        })
        return Completable.concat(listOf(putRelease, putSchedule))
    }

    override fun removeList(items: List<ScheduleDay>): Completable = scheduleCache
        .removeList(items.map {
            val releaseIds = it.items.map { release -> release.id }
            ScheduleDayRelative(it.day, releaseIds)
        })

    override fun clear(): Completable = scheduleCache.clear()
}