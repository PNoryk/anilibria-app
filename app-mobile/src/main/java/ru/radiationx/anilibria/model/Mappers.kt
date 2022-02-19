package ru.radiationx.anilibria.model

import ru.radiationx.anilibria.ui.fragments.other.OtherMenuItemState
import ru.radiationx.anilibria.ui.fragments.other.ProfileItemState
import ru.radiationx.data.entity.app.other.OtherMenuItem
import ru.radiationx.data.entity.app.other.ProfileItem
import ru.radiationx.data.entity.common.AuthState
import tv.anilibria.module.domain.entity.auth.SocialAuthService
import tv.anilibria.module.domain.entity.feed.Feed
import tv.anilibria.module.domain.entity.release.Release
import tv.anilibria.module.domain.entity.youtube.Youtube

//todo map isnew
fun Release.toState(): ReleaseItemState {
    val rusTitle = names?.firstOrNull()
    val title = if (series == null) {
        rusTitle.toString()
    } else {
        "$rusTitle ($series)"
    }
    return ReleaseItemState(
        id = id,
        title = title,
        description = description?.text.orEmpty(),
        posterUrl = poster?.url.orEmpty(),
        isNew = false
    )
}

fun Youtube.toState() = YoutubeItemState(
    id = id,
    title = title?.text.orEmpty(),
    image = image?.url.orEmpty(),
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
    posterUrl = poster?.url.orEmpty(),
    isCompleted = true
)

fun ProfileItem.toState(): ProfileItemState {
    val hasAuth = authState == AuthState.AUTH
    val title = if (hasAuth) {
        nick
    } else {
        "Гость"
    }
    val subtitle = if (hasAuth) {
        null
    } else {
        "Авторизоваться"
    }
    val avatar = avatarUrl?.takeIf { it.isNotEmpty() } ?: "assets://res/alib_new_or_b.png"
    return ProfileItemState(
        id = id,
        hasAuth = hasAuth,
        title = title,
        subtitle = subtitle,
        avatar = avatar
    )
}

fun OtherMenuItem.toState() = OtherMenuItemState(
    id = id,
    title = title,
    iconRes = icon
)

fun SocialAuthService.toState(): SocialAuthItemState = SocialAuthItemState(
    key = key,
    title = title,
    iconRes = icon.asDataIconRes(),
    colorRes = color.asDataColorRes()
)

fun Release.toSuggestionState(query: String): SuggestionItemState {
    val itemTitle = names?.firstOrNull()?.text.orEmpty()
    val matchRanges = try {
        Regex(query, RegexOption.IGNORE_CASE).findAll(itemTitle).map { it.range }.toList()
    } catch (ignore: Throwable) {
        emptyList<IntRange>()
    }

    return SuggestionItemState(
        id,
        itemTitle,
        poster?.url.orEmpty(),
        matchRanges
    )
}