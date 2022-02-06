package tv.anilibria.module.data.network.datasource.holders

import io.reactivex.Observable
import tv.anilibria.module.data.network.entity.app.release.YearItemResponse

interface YearsHolder {
    fun observeYears(): Observable<MutableList<YearItemResponse>>
    fun saveYears(genres: List<YearItemResponse>)
    fun getYears(): List<YearItemResponse>
}