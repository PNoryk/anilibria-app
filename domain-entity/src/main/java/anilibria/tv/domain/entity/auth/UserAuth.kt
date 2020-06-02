package anilibria.tv.domain.entity.auth

import anilibria.tv.domain.entity.user.User

data class UserAuth(
    val state: State,
    val user: User?
) {

    init {
        if ((state == State.AUTH && user == null) || (state == State.NO_AUTH && user != null)) {
            throw IllegalStateException("AuthUser is broken $state, ${user?.id}")
        }
    }

    enum class State {
        NO_AUTH, AUTH
    }
}