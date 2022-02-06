package tv.anilibria.module.data.network.datasource.holders

import io.reactivex.Observable
import tv.anilibria.module.data.network.entity.app.release.GenreItemResponse

interface GenresHolder {
    fun observeGenres(): Observable<MutableList<GenreItemResponse>>
    fun saveGenres(genres: List<GenreItemResponse>)
    fun getGenres(): List<GenreItemResponse>
}