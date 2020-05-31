package anilibria.tv.domain.entity.auth

import anilibria.tv.domain.entity.user.User

data class UserAuth(
    val state: State,
    val user: User?
) {

    enum class State {
        NO_AUTH, AUTH_SKIPPED, AUTH
    }
}