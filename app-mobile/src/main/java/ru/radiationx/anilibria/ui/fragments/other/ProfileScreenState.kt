package ru.radiationx.anilibria.ui.fragments.other

import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.module.domain.entity.other.UserId

data class ProfileScreenState(
    val profile: ProfileItemState? = null,
    val menuItems: List<List<OtherMenuItemState>> = emptyList()
)

data class ProfileItemState(
    val id: UserId?,
    val hasAuth: Boolean,
    val title: String,
    val subtitle: String?,
    val avatar: AbsoluteUrl
)

data class OtherMenuItemState(
    val id: Int,
    val title: String,
    val iconRes: Int
)