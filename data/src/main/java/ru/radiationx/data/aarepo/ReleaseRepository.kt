package ru.radiationx.data.aarepo

import io.reactivex.Observable
import io.reactivex.Single
import ru.radiationx.data.acache.combiner.ReleaseCacheCombiner
import ru.radiationx.data.adomain.entity.feed.Feed
import ru.radiationx.data.adomain.entity.pagination.Paginated
import ru.radiationx.data.adomain.entity.release.RandomRelease
import ru.radiationx.data.adomain.entity.release.Release
import ru.radiationx.data.api.common.handleApiResponse
import ru.radiationx.data.api.datasource.ReleaseApiDataSource
import toothpick.InjectConstructor

@InjectConstructor
class ReleaseRepository(
    private val apiDataSource: ReleaseApiDataSource,
    private val cacheCombiner: ReleaseCacheCombiner
) {

    fun observeOne(releaseId: Int? = null, releaseCode: String? = null): Observable<Release> = cacheCombiner
        .observeList()
        .map {
            it.firstOrNull { release ->
                release.id == releaseId || release.code == releaseCode
            }
        }

    fun observeSome(ids: List<Int>? = null, codes: List<String>? = null): Observable<List<Release>> = cacheCombiner
        .observeList()
        .map {
            it.filter { release ->
                ids?.contains(release.id) == true || codes?.contains(release.code) == true
            }
        }

    fun observeList(): Observable<List<Release>> = cacheCombiner.observeList()

    fun getOne(releaseId: Int? = null, releaseCode: String? = null): Single<Release> = apiDataSource
        .getOne(releaseId, releaseCode)
        .flatMap { cacheCombiner.putOne(it).toSingleDefault(it) }

    fun getSome(ids: List<Int>? = null, codes: List<String>? = null): Single<List<Release>> = apiDataSource
        .getSome(ids, codes)
        .flatMap { cacheCombiner.putList(it).toSingleDefault(it) }

    fun getList(page: Int): Single<List<Release>> = apiDataSource
        .getList(page)
        .flatMap {
            if (page == 1) {
                cacheCombiner.clear().toSingleDefault(it)
            } else {
                cacheCombiner.putList(it.items).toSingleDefault(it)
            }
        }
        .flatMap { cacheCombiner.fetchList() }

    fun getRandom(): Single<RandomRelease> = apiDataSource
        .getRandom()
        .onErrorResumeNext { fetchLocalRandom() }

    private fun fetchLocalRandom(): Single<RandomRelease> = cacheCombiner
        .fetchList()
        .map { it.mapNotNull { release -> release.code }.random() }
        .map { RandomRelease(it) }
}