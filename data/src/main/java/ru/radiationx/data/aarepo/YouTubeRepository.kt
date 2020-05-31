package ru.radiationx.data.aarepo

import io.reactivex.Observable
import io.reactivex.Single
import ru.radiationx.data.acache.YoutubeCache
import ru.radiationx.data.adomain.entity.youtube.Youtube
import ru.radiationx.data.api.datasource.YouTubeApiDataSourceImpl
import toothpick.InjectConstructor

@InjectConstructor
class YouTubeRepository(
    private val apiDataSource: YouTubeApiDataSourceImpl,
    private val youtubeCache: YoutubeCache
) {

    fun observeList(): Observable<List<Youtube>> = youtubeCache.observeList()

    fun getList(page: Int): Single<List<Youtube>> = apiDataSource
        .getList(page)
        .flatMap {
            if (page == 1) {
                youtubeCache.clear().toSingleDefault(it)
            } else {
                youtubeCache.putList(it.items).toSingleDefault(it)
            }
        }
        .flatMap { youtubeCache.getList() }
}