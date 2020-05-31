package ru.radiationx.data.aarepo

import io.reactivex.Observable
import io.reactivex.Single
import anilibria.tv.cache.YoutubeCache
import anilibria.tv.domain.entity.youtube.Youtube
import anilibria.tv.api.YouTubeApiDataSource
import toothpick.InjectConstructor

@InjectConstructor
class YouTubeRepository(
    private val apiDataSource: YouTubeApiDataSource,
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