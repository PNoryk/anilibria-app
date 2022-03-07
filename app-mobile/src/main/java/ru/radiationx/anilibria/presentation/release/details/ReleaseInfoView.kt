package ru.radiationx.anilibria.presentation.release.details

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType
import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.core.types.RelativeUrl
import tv.anilibria.feature.player.data.prefs.PrefferedPlayerQuality
import tv.anilibria.feature.content.types.release.Episode
import tv.anilibria.feature.content.types.release.Release

@StateStrategyType(AddToEndSingleStrategy::class)
interface ReleaseInfoView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showState(state: ReleaseDetailScreenState)

    @StateStrategyType(SkipStrategy::class)
    fun playEpisodes(release: Release)

    @StateStrategyType(SkipStrategy::class)
    fun playContinue(release: Release, startWith: Episode)

    @StateStrategyType(SkipStrategy::class)
    fun playWeb(link: AbsoluteUrl, releaseLink: RelativeUrl)

    @StateStrategyType(SkipStrategy::class)
    fun playEpisode(
        release: Release,
        episode: Episode,
        playFlag: Int? = null,
        quality: PrefferedPlayerQuality? = null
    )

    @StateStrategyType(SkipStrategy::class)
    fun downloadEpisode(
        episode: Episode,
        quality: PrefferedPlayerQuality? = null
    )

    @StateStrategyType(SkipStrategy::class)
    fun showFavoriteDialog()

    @StateStrategyType(SkipStrategy::class)
    fun showDownloadDialog(url: AbsoluteUrl?)

    @StateStrategyType(SkipStrategy::class)
    fun showFileDonateDialog(url: AbsoluteUrl?)

    @StateStrategyType(SkipStrategy::class)
    fun showEpisodesMenuDialog()

    @StateStrategyType(SkipStrategy::class)
    fun showLongPressEpisodeDialog(episode: Episode)
}