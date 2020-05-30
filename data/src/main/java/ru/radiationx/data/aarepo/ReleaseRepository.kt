package ru.radiationx.data.aarepo

import io.reactivex.Observable
import io.reactivex.Single
import ru.radiationx.data.acache.combiner.ReleaseCacheCombiner
import ru.radiationx.data.adomain.entity.release.RandomRelease
import ru.radiationx.data.adomain.entity.release.Release
import ru.radiationx.data.api.datasource.ReleaseApiDataSource
import toothpick.InjectConstructor

@InjectConstructor
class ReleaseRepository(
    private val apiDataSource: ReleaseApiDataSource,
    private val cacheCombiner: ReleaseCacheCombiner
) {

    fun observeOne(releaseId: Int? = null, releaseCode: String? = null): Observable<Release> =
        cacheCombiner.observeOne(releaseId, releaseCode)

    fun observeList(ids: List<Int>? = null, codes: List<String>? = null): Observable<List<Release>> = cacheCombiner
        .observeList(ids, codes)

    fun observeList(): Observable<List<Release>> = cacheCombiner.observeList()

    fun getList(ids: List<Int>? = null, codes: List<String>? = null): Single<List<Release>> = apiDataSource
        .getSome(ids, codes)
        .flatMap { cacheCombiner.putList(it).toSingleDefault(it) }
        .flatMap { cacheCombiner.getList(ids, codes) }

    fun getOne(releaseId: Int? = null, releaseCode: String? = null): Single<Release> = apiDataSource
        .getOne(releaseId, releaseCode)
        .flatMap { cacheCombiner.putList(listOf(it)).toSingleDefault(it) }
        .flatMap { cacheCombiner.getOne(releaseId, releaseCode) }

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

    private fun fetchLocalRandom(): Single<RandomRelease> = cacheCombiner
        .getList()
        .map { it.mapNotNull { release -> release.code }.random() }
        .map { RandomRelease(it) }
}