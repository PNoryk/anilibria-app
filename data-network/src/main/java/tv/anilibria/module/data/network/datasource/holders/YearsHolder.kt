package tv.anilibria.module.data.network.datasource.holders

import io.reactivex.Observable
import tv.anilibria.module.data.network.entity.app.release.YearItem

interface YearsHolder {
    fun observeYears(): Observable<MutableList<YearItem>>
    fun saveYears(genres: List<YearItem>)
    fun getYears(): List<YearItem>
}