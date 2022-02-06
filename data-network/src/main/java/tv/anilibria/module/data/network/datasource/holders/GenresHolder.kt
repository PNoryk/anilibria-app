package tv.anilibria.module.data.network.datasource.holders

import io.reactivex.Observable
import tv.anilibria.module.data.network.entity.app.release.GenreItem

interface GenresHolder {
    fun observeGenres(): Observable<MutableList<GenreItem>>
    fun saveGenres(genres: List<GenreItem>)
    fun getGenres(): List<GenreItem>
}