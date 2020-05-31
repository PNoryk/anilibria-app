package ru.radiationx.data.acache.combiner.impl

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function
import ru.radiationx.data.acache.ScheduleCache
import ru.radiationx.data.acache.combiner.ReleaseCacheCombiner
import ru.radiationx.data.acache.combiner.ScheduleCacheCombiner
import anilibria.tv.domain.entity.relative.ScheduleDayRelative
import anilibria.tv.domain.entity.release.Release
import anilibria.tv.domain.entity.schedule.ScheduleDay
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

    override fun observeList(): Observable<List<ScheduleDay>> = scheduleCache
        .observeList()
        .switchMap { relativeItems ->
            releaseCache
                .observeList(relativeItems.map { it.releaseIds }.flatten())
                .map(getSourceCombiner(relativeItems))
        }

    override fun getList(): Single<List<ScheduleDay>> = scheduleCache
        .getList()
        .flatMap { relativeItems ->
            releaseCache
                .getList(relativeItems.map { it.releaseIds }.flatten())
                .map(getSourceCombiner(relativeItems))
        }

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

    private fun getSourceCombiner(relativeItems: List<ScheduleDayRelative>) = Function<List<Release>, List<ScheduleDay>> { releaseItems ->
        relativeItems.map { relative ->
            val releases = relative.releaseIds.mapNotNull { releaseId ->
                releaseItems.firstOrNull { it.id == releaseId }
            }
            ScheduleDay(relative.dayId, releases)
        }
    }
}