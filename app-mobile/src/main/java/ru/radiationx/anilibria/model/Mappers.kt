package ru.radiationx.anilibria.model

import ru.radiationx.anilibria.ui.fragments.other.ProfileItemState
import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.feature.auth.data.domain.AuthState
import tv.anilibria.feature.auth.data.domain.SocialAuthService
import tv.anilibria.feature.user.data.domain.User
import tv.anilibria.feature.data.BaseUrlHelper
import tv.anilibria.feature.domain.entity.feed.Feed
import tv.anilibria.feature.domain.entity.release.Release
import tv.anilibria.feature.domain.entity.youtube.Youtube

//todo map isnew
fun Release.toState(urlHelper: BaseUrlHelper): ReleaseItemState {
    val rusTitle = nameRus?.text
    val title = if (series == null) {
        rusTitle.toString()
    } else {
        "$rusTitle ($series)"
    }
    return ReleaseItemState(
        id = id,
        title = title,
        description = description?.text.orEmpty(),
        posterUrl = urlHelper.makeMedia(poster),
        isNew = false
    )
}

fun Youtube.toState(urlHelper: BaseUrlHelper) = YoutubeItemState(
    id = id,
    title = title?.text.orEmpty(),
    image = urlHelper.makeMedia(image),
    views = views.value.toString(),
    comments = comments.value.toString()
)

fun Feed.toState(urlHelper: BaseUrlHelper) = FeedItemState(
    release = release?.toState(urlHelper),
    youtube = youtube?.toState(urlHelper)
)

//todo map iscompleted
fun Release.toScheduleState(urlHelper: BaseUrlHelper) = ScheduleItemState(
    releaseId = id,
    posterUrl = urlHelper.makeMedia(poster),
    isCompleted = true
)

fun User.toState(urlHelper: BaseUrlHelper, authState: AuthState): ProfileItemState {
    val hasAuth = authState == AuthState.AUTH
    val title = if (hasAuth) {
        login
    } else {
        "Гость"
    }
    val subtitle = if (hasAuth) {
        null
    } else {
        "Авторизоваться"
    }
    val avatar = urlHelper.makeMedia(avatar) ?: AbsoluteUrl("assets://res/alib_new_or_b.png")
    return ProfileItemState(
        id = id,
        hasAuth = hasAuth,
        title = title,
        subtitle = subtitle,
        avatar = avatar
    )
}

fun SocialAuthService.toState(): SocialAuthItemState = SocialAuthItemState(
    key = key,
    title = title,
    iconRes = icon.asDataIconRes(),
    colorRes = color.asDataColorRes()
)

fun Release.toSuggestionState(urlHelper: BaseUrlHelper, query: String): SuggestionItemState {
    val itemTitle = nameRus?.text.orEmpty()
    val matchRanges = try {
        Regex(query, RegexOption.IGNORE_CASE).findAll(itemTitle).map { it.range }.toList()
    } catch (ignore: Throwable) {
        emptyList<IntRange>()
    }

    return SuggestionItemState(
        id = id,
        title = itemTitle,
        poster = urlHelper.makeMedia(poster),
        matchRanges = matchRanges
    )
}