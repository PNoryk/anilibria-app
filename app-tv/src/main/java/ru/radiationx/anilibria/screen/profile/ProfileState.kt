package ru.radiationx.anilibria.screen.profile

import tv.anilibria.module.domain.entity.AuthState
import tv.anilibria.module.domain.entity.other.User

data class ProfileState(
    val user: User,
    val authState: AuthState
)
