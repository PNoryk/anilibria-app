package anilibria.tv.cache.impl.memory

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import anilibria.tv.domain.entity.release.Release
import toothpick.InjectConstructor
import java.util.*

@InjectConstructor
class ReleaseMemoryDataSource {

    private val memory = Collections.synchronizedList(mutableListOf<Release>())
    private val memoryMapId = Collections.synchronizedMap(mutableMapOf<Int, Release>())
    private val memoryMapCode = Collections.synchronizedMap(mutableMapOf<String, Release>())

    private val dataRelay by lazy { BehaviorRelay.createDefault(memory.toList()) }

    fun observeListAll(): Observable<List<Release>> = dataRelay.hide()

    fun observeList(ids: List<Int>?, codes: List<String>?): Observable<List<Release>> = dataRelay
        .map { getListFromMap(ids, codes) }

    fun observeOne(releaseId: Int?, releaseCode: String?): Observable<Release> = dataRelay
        .filter {
            val hasId = releaseId?.let { memoryMapId.containsKey(it) } == true
            val hasCode = releaseCode?.let { memoryMapCode.containsKey(it) } == true
            hasId || hasCode
        }
        .map { getOneFromMap(releaseId, releaseCode) }

    fun getListAll(): Single<List<Release>> = Single.fromCallable {
        memory.toList()
    }

    fun getList(ids: List<Int>?, codes: List<String>?): Single<List<Release>> = Single.fromCallable {
        getListFromMap(ids, codes)
    }

    fun getOne(releaseId: Int?, releaseCode: String?): Single<Release> = Single.fromCallable {
        getOneFromMap(releaseId, releaseCode)!!
    }

    fun insert(items: List<Release>): Completable = Completable.fromAction {
        synchronized(this) {
            items.forEach { new ->
                memory.removeAll { old ->
                    old.id == new.id
                }
            }
            memory.addAll(items)
        }
        updateRelay()
    }

    fun removeList(ids: List<Int>): Completable = Completable.fromAction {
        synchronized(this) {
            ids.forEach { id ->
                memory.removeAll { old -> old.id == id }
            }
            updateMap()
        }
        updateRelay()
    }

    fun deleteAll(): Completable = Completable.fromAction {
        synchronized(this) {
            memory.clear()
            updateMap()
        }
        updateRelay()
    }

    private fun getListFromMap(ids: List<Int>?, codes: List<String>?): List<Release> {
        val result = mutableListOf<Release>()
        ids?.mapNotNull { memoryMapId[it] }?.also { result.addAll(it) }
        codes?.mapNotNull { memoryMapCode[it] }?.also { result.addAll(it) }
        return result.distinctBy { it.id }
    }

    private fun getOneFromMap(releaseId: Int?, releaseCode: String?): Release? {
        val fromId = memoryMapId[releaseId]
        val fromCode = memoryMapCode[releaseCode]
        return fromId ?: fromCode
    }

    private fun updateMap() {
        memoryMapId.clear()
        memoryMapCode.clear()
        memory.forEach { release ->
            memoryMapId[release.id] = release
            release.code?.also {
                memoryMapCode[it] = release
            }
        }
    }

    private fun updateRelay() {
        dataRelay.accept(memory.toList())
    }
}