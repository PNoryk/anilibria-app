package ru.radiationx.anilibria.screen.main

import android.util.Log
import ru.radiationx.anilibria.common.BaseCardsViewModel
import ru.radiationx.anilibria.common.CardsDataConverter
import ru.radiationx.anilibria.common.LibriaCard
import ru.radiationx.anilibria.screen.DetailsScreen
import ru.radiationx.shared_app.common.SystemUtils
import ru.terrakok.cicerone.Router
import toothpick.InjectConstructor
import tv.anilibria.module.data.repos.FeedRepository
import tv.anilibria.module.domain.entity.release.ReleaseId
import tv.anilibria.module.domain.entity.youtube.Youtube

@InjectConstructor
class MainFeedViewModel(
    private val feedRepository: FeedRepository,
    private val converter: CardsDataConverter,
    private val router: Router,
    private val systemUtils: SystemUtils
) : BaseCardsViewModel() {

    override val defaultTitle: String = "Самое актуальное"

    override val loadOnCreate: Boolean = false

    override fun onColdCreate() {
        super.onColdCreate()
        onRefreshClick()
        Log.e("kekeke", "onColdCreate ${this::class.java.simpleName}")
    }

    override fun onCreate() {
        super.onCreate()
        Log.e("kekeke", "onCreate ${this::class.java.simpleName}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("kekeke", "onDestroy ${this::class.java.simpleName}")
    }

    override fun onCleared() {
        super.onCleared()
        Log.e("kekeke", "onCleared ${this::class.java.simpleName}")
    }

    override suspend fun getCoLoader(requestPage: Int): List<LibriaCard> = feedRepository
        .getFeed(requestPage)
        .let { feedList -> feedList.map { converter.toCard(it) } }

    override fun onLibriaCardClick(card: LibriaCard) {
        super.onLibriaCardClick(card)
        when (card.type) {
            LibriaCard.Type.RELEASE -> {
                router.navigateTo(DetailsScreen(ReleaseId(card.id)))
            }
            LibriaCard.Type.YOUTUBE -> {
                val youtubeItem = card.rawData as Youtube
                youtubeItem.link?.value?.also { systemUtils.externalLink(it) }
            }
        }
    }
}