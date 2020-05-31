package anilibria.tv.db.impl.datasource

import anilibria.tv.db.ReleaseDbDataSource
import io.reactivex.Completable
import io.reactivex.Single
import anilibria.tv.db.impl.converters.ReleaseConverter
import anilibria.tv.db.impl.dao.ReleaseDao
import anilibria.tv.domain.entity.release.Release
import toothpick.InjectConstructor

@InjectConstructor
class ReleaseDbDataSourceImpl(
    private val dao: ReleaseDao,
    private val converter: ReleaseConverter
) : ReleaseDbDataSource {

    override fun getListAll(): Single<List<Release>> = dao
        .getListAll()
        .map(converter::toDomain)

    override fun getList(ids: List<Int>?, codes: List<String>?): Single<List<Release>> = dao
        .getList(ids.orEmpty(), codes.orEmpty())
        .map(converter::toDomain)

    override fun getOne(releaseId: Int?, code: String?): Single<Release> = dao
        .getOne(releaseId, code)
        .map(converter::toDomain)

    override fun insert(items: List<Release>): Completable = Single.just(items)
        .map(converter::toDb)
        .flatMapCompletable(dao::insert)

    override fun removeList(ids: List<Int>): Completable = dao.delete(ids)

    override fun deleteAll(): Completable = dao.deleteAll()
}