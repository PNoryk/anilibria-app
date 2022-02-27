package ru.radiationx.anilibria.screen.profile

import tv.anilibria.feature.auth.data.domain.AuthState
import tv.anilibria.feature.user.data.domain.User

data class ProfileState(
    val user: User,
    val authState: AuthState
)
