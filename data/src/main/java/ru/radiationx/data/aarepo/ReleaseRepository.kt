package ru.radiationx.data.aarepo

import io.reactivex.Observable
import io.reactivex.Single
import anilibria.tv.cache.combiner.ReleaseCacheCombiner
import anilibria.tv.domain.entity.release.RandomRelease
import anilibria.tv.domain.entity.release.Release
import anilibria.tv.api.ReleaseApiDataSource
import anilibria.tv.cache.ReleaseCache
import anilibria.tv.domain.entity.common.keys.ReleaseKey
import toothpick.InjectConstructor

@InjectConstructor
class ReleaseRepository(
    private val apiDataSource: ReleaseApiDataSource,
    private val cacheCombiner: ReleaseCacheCombiner,
    private val releaseCache: ReleaseCache
) {

    fun observeOne(id: Int): Observable<Release> = cacheCombiner.observeOne(id.toKey())

    fun observeSome(ids: List<Int>): Observable<List<Release>> = cacheCombiner.observeSome(ids.toKeys())

    fun observeList(): Observable<List<Release>> = cacheCombiner.observeList()

    fun getSome(ids: List<Int>): Single<List<Release>> = apiDataSource
        .getSome(ids)
        .flatMap { cacheCombiner.putList(it).toSingleDefault(it) }
        .flatMap { cacheCombiner.getSome(ids.toKeys()) }

    fun getOne(id: Int): Single<Release> = apiDataSource
        .getOne(id)
        .flatMap { cacheCombiner.putList(listOf(it)).toSingleDefault(it) }
        .flatMap { cacheCombiner.getOne(id.toKey()) }

    fun getList(page: Int): Single<List<Release>> = apiDataSource
        .getList(page)
        .flatMap {
            if (page == 1) {
                cacheCombiner.clear().toSingleDefault(it)
            } else {
                cacheCombiner.putList(it.items).toSingleDefault(it)
            }
        }
        .flatMap { cacheCombiner.getList() }

    fun getRandom(): Single<RandomRelease> = apiDataSource
        .getRandom()
        .onErrorResumeNext { fetchLocalRandom() }

    fun getIdByCode(code: String): Single<Int> = releaseCache
        .getList()
        .flatMap {
            val foundRelease = it.firstOrNull { it.code == code }
            if (foundRelease != null) {
                Single.just(foundRelease.id)
            } else {
                apiDataSource
                    .getOne(releaseCode = code)
                    .flatMap { cacheCombiner.putList(listOf(it)).toSingleDefault(it) }
                    .map { it.id }
            }
        }

    private fun fetchLocalRandom(): Single<RandomRelease> = releaseCache
        .getList()
        .map { it.mapNotNull { release -> release.code }.random() }
        .map { RandomRelease(it) }

    private fun Int.toKey() = ReleaseKey(this)

    private fun List<Int>.toKeys() = map { ReleaseKey(it) }
}