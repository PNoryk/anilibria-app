package ru.radiationx.anilibria.screen.player

import ru.radiationx.anilibria.common.fragment.scoped.ScopedGuidedStepFragment
import ru.radiationx.anilibria.screen.player.BasePlayerGuidedFragment.Companion.ARG_EPISODE_ID
import ru.radiationx.shared.ktx.android.putExtra
import tv.anilibria.feature.content.types.release.EpisodeId

abstract class BasePlayerGuidedFragment : ScopedGuidedStepFragment() {

    companion object {
        const val ARG_EPISODE_ID = "episode id"
    }

    protected val episodeId by lazy {
        requireNotNull(arguments?.getParcelable<EpisodeId>(ARG_EPISODE_ID))
    }
}

fun <T : BasePlayerGuidedFragment> T.putEpisodeId(episodeId: EpisodeId): T = putExtra {
    putParcelable(ARG_EPISODE_ID, episodeId)
}