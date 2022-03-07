package ru.radiationx.anilibria.screen

import androidx.fragment.app.Fragment
import ru.radiationx.anilibria.common.fragment.FakeGuidedStepFragment
import ru.radiationx.anilibria.common.fragment.GuidedAppScreen
import ru.radiationx.anilibria.screen.auth.credentials.AuthCredentialsGuidedFragment
import ru.radiationx.anilibria.screen.auth.main.AuthGuidedFragment
import ru.radiationx.anilibria.screen.auth.otp.AuthOtpGuidedFragment
import ru.radiationx.anilibria.screen.config.ConfigFragment
import ru.radiationx.anilibria.screen.details.DetailFragment
import ru.radiationx.anilibria.screen.mainpages.MainPagesFragment
import ru.radiationx.anilibria.screen.player.PlayerFragment
import ru.radiationx.anilibria.screen.player.end_episode.EndEpisodeGuidedFragment
import ru.radiationx.anilibria.screen.player.end_season.EndSeasonGuidedFragment
import ru.radiationx.anilibria.screen.player.episodes.PlayerEpisodesGuidedFragment
import ru.radiationx.anilibria.screen.player.putEpisodeId
import ru.radiationx.anilibria.screen.player.quality.PlayerQualityGuidedFragment
import ru.radiationx.anilibria.screen.player.speed.PlayerSpeedGuidedFragment
import ru.radiationx.anilibria.screen.schedule.ScheduleFragment
import ru.radiationx.anilibria.screen.search.SearchFragment
import ru.radiationx.anilibria.screen.search.completed.SearchCompletedGuidedFragment
import ru.radiationx.anilibria.screen.search.genre.SearchGenreGuidedFragment
import ru.radiationx.anilibria.screen.search.putValues
import ru.radiationx.anilibria.screen.search.season.SearchSeasonGuidedFragment
import ru.radiationx.anilibria.screen.search.sort.SearchSortGuidedFragment
import ru.radiationx.anilibria.screen.search.year.SearchYearGuidedFragment
import ru.radiationx.anilibria.screen.suggestions.SuggestionsFragment
import ru.radiationx.anilibria.screen.trash.TestFlowFragment
import ru.radiationx.anilibria.screen.trash.TestFragment
import ru.radiationx.anilibria.screen.trash.VerticalGridTestFragment
import ru.radiationx.anilibria.screen.update.UpdateFragment
import ru.radiationx.anilibria.screen.update.source.UpdateSourceGuidedFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen
import tv.anilibria.feature.content.types.ReleaseGenre
import tv.anilibria.feature.content.types.ReleaseSeason
import tv.anilibria.feature.content.types.ReleaseYear
import tv.anilibria.feature.content.types.SearchForm
import tv.anilibria.feature.content.types.release.EpisodeId
import tv.anilibria.feature.content.types.release.ReleaseId

class ConfigScreen() : SupportAppScreen() {
    override fun getFragment(): Fragment {
        return ConfigFragment()
    }
}

class MainPagesScreen() : SupportAppScreen() {
    override fun getFragment(): Fragment {
        return MainPagesFragment()
    }
}

class GridScreen() : SupportAppScreen() {
    override fun getFragment(): Fragment {
        return VerticalGridTestFragment()
    }
}

class DetailsScreen(private val releaseId: ReleaseId) : SupportAppScreen() {
    override fun getFragment(): Fragment {
        return DetailFragment.newInstance(releaseId)
    }
}

class ScheduleScreen() : SupportAppScreen() {
    override fun getFragment(): Fragment {
        return ScheduleFragment()
    }
}

class UpdateScreen() : SupportAppScreen() {
    override fun getFragment(): Fragment {
        return UpdateFragment()
    }
}

class UpdateSourceScreen() : GuidedAppScreen() {
    override fun getFragment(): FakeGuidedStepFragment {
        return UpdateSourceGuidedFragment()
    }
}

class SuggestionsScreen() : SupportAppScreen() {
    override fun getFragment(): Fragment {
        return SuggestionsFragment()
    }
}

class SearchScreen() : SupportAppScreen() {
    override fun getFragment(): Fragment {
        return SearchFragment()
    }
}

class SearchYearGuidedScreen(private val values: List<ReleaseYear>) : GuidedAppScreen() {
    override fun getFragment(): FakeGuidedStepFragment {
        return SearchYearGuidedFragment().putValues(values.map { it.value })
    }
}

class SearchSeasonGuidedScreen(private val values: List<ReleaseSeason>) : GuidedAppScreen() {
    override fun getFragment(): FakeGuidedStepFragment {
        return SearchSeasonGuidedFragment().putValues(values.map { it.value })
    }
}

class SearchGenreGuidedScreen(private val values: List<ReleaseGenre>) : GuidedAppScreen() {
    override fun getFragment(): FakeGuidedStepFragment {
        return SearchGenreGuidedFragment().putValues(values.map { it.value })
    }
}

class SearchSortGuidedScreen(private val sort: SearchForm.Sort) : GuidedAppScreen() {
    override fun getFragment(): FakeGuidedStepFragment {
        return SearchSortGuidedFragment.newInstance(sort)
    }
}

class SearchCompletedGuidedScreen(private val onlyCompleted: Boolean) : GuidedAppScreen() {
    override fun getFragment(): FakeGuidedStepFragment {
        return SearchCompletedGuidedFragment.newInstance(onlyCompleted)
    }
}

class FlowScreen() : SupportAppScreen() {
    override fun getFragment(): Fragment {
        return TestFlowFragment()
    }
}

class TestScreen() : SupportAppScreen() {
    override fun getFragment(): Fragment {
        return TestFragment()
    }
}

class AuthGuidedScreen : GuidedAppScreen() {
    override fun getFragment(): FakeGuidedStepFragment {
        return AuthGuidedFragment()
    }
}

class AuthCredentialsGuidedScreen : GuidedAppScreen() {
    override fun getFragment(): FakeGuidedStepFragment {
        return AuthCredentialsGuidedFragment()
    }
}

class AuthOtpGuidedScreen : GuidedAppScreen() {
    override fun getFragment(): FakeGuidedStepFragment {
        return AuthOtpGuidedFragment()
    }
}

class PlayerScreen(val episodeId: EpisodeId) : SupportAppScreen() {
    override fun getFragment(): Fragment {
        return PlayerFragment.newInstance(episodeId)
    }
}

class PlayerQualityGuidedScreen(val episodeId: EpisodeId) : GuidedAppScreen() {
    override fun getFragment(): FakeGuidedStepFragment {
        return PlayerQualityGuidedFragment().putEpisodeId(episodeId)
    }
}

class PlayerSpeedGuidedScreen(val episodeId: EpisodeId) : GuidedAppScreen() {
    override fun getFragment(): FakeGuidedStepFragment {
        return PlayerSpeedGuidedFragment().putEpisodeId(episodeId)
    }
}

class PlayerEpisodesGuidedScreen(val episodeId: EpisodeId) : GuidedAppScreen() {
    override fun getFragment(): FakeGuidedStepFragment {
        return PlayerEpisodesGuidedFragment().putEpisodeId(episodeId)
    }
}

class PlayerEndEpisodeGuidedScreen(val episodeId: EpisodeId) : GuidedAppScreen() {
    override fun getFragment(): FakeGuidedStepFragment {
        return EndEpisodeGuidedFragment().putEpisodeId(episodeId)
    }
}

class PlayerEndSeasonGuidedScreen(val episodeId: EpisodeId) : GuidedAppScreen() {
    override fun getFragment(): FakeGuidedStepFragment {
        return EndSeasonGuidedFragment().putEpisodeId(episodeId)
    }
}

class TestGuidedStepScreen : GuidedAppScreen() {
    override fun getFragment(): FakeGuidedStepFragment {
        return DialogExampleFragment()
    }
}