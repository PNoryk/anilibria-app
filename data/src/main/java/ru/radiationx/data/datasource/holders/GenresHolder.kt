package ru.radiationx.data.datasource.holders

import io.reactivex.Observable
import ru.radiationx.data.entity.app.release.GenreItem

@Deprecated("old data")
interface GenresHolder {
    fun observeGenres(): Observable<MutableList<GenreItem>>
    fun saveGenres(genres: List<GenreItem>)
    fun getGenres(): List<GenreItem>
}