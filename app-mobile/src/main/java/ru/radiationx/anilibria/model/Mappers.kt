package ru.radiationx.anilibria.model

import ru.radiationx.anilibria.ui.fragments.other.ProfileItemState
import tv.anilibria.module.domain.entity.AuthState
import tv.anilibria.module.domain.entity.auth.SocialAuthService
import tv.anilibria.module.domain.entity.feed.Feed
import tv.anilibria.module.domain.entity.other.User
import tv.anilibria.module.domain.entity.release.Release
import tv.anilibria.module.domain.entity.youtube.Youtube

//todo map isnew
fun Release.toState(): ReleaseItemState {
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
        posterUrl = poster?.value.orEmpty(),
        isNew = false
    )
}

fun Youtube.toState() = YoutubeItemState(
    id = id,
    title = title?.text.orEmpty(),
    image = image?.value.orEmpty(),
    views = views.value.toString(),
    comments = comments.value.toString()
)

fun Feed.toState() = FeedItemState(
    release = release?.toState(),
    youtube = youtube?.toState()
)

//todo map iscompleted
fun Release.toScheduleState() = ScheduleItemState(
    releaseId = id,
    posterUrl = poster?.value.orEmpty(),
    isCompleted = true
)

fun User.toState(authState: AuthState): ProfileItemState {
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
    val avatar = avatar?.value?.takeIf { it.isNotEmpty() } ?: "assets://res/alib_new_or_b.png"
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

fun Release.toSuggestionState(query: String): SuggestionItemState {
    val itemTitle = nameRus?.text.orEmpty()
    val matchRanges = try {
        Regex(query, RegexOption.IGNORE_CASE).findAll(itemTitle).map { it.range }.toList()
    } catch (ignore: Throwable) {
        emptyList<IntRange>()
    }

    return SuggestionItemState(
        id,
        itemTitle,
        poster?.value.orEmpty(),
        matchRanges
    )
}