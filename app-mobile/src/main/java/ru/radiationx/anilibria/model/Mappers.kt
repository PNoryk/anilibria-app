package ru.radiationx.anilibria.model

import ru.radiationx.anilibria.ui.fragments.other.ProfileItemState
import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.module.data.UrlHelper
import tv.anilibria.module.domain.entity.AuthState
import tv.anilibria.module.domain.entity.auth.SocialAuthService
import tv.anilibria.module.domain.entity.feed.Feed
import tv.anilibria.module.domain.entity.other.User
import tv.anilibria.module.domain.entity.release.Release
import tv.anilibria.module.domain.entity.youtube.Youtube

//todo map isnew
fun Release.toState(urlHelper: UrlHelper): ReleaseItemState {
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

fun Youtube.toState(urlHelper: UrlHelper) = YoutubeItemState(
    id = id,
    title = title?.text.orEmpty(),
    image = urlHelper.makeMedia(image),
    views = views.value.toString(),
    comments = comments.value.toString()
)

fun Feed.toState(urlHelper: UrlHelper) = FeedItemState(
    release = release?.toState(urlHelper),
    youtube = youtube?.toState(urlHelper)
)

//todo map iscompleted
fun Release.toScheduleState(urlHelper: UrlHelper) = ScheduleItemState(
    releaseId = id,
    posterUrl = urlHelper.makeMedia(poster),
    isCompleted = true
)

fun User.toState(urlHelper: UrlHelper, authState: AuthState): ProfileItemState {
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

fun Release.toSuggestionState(urlHelper: UrlHelper, query: String): SuggestionItemState {
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