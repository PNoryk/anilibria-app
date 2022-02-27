package ru.radiationx.anilibria.screen.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.radiationx.anilibria.common.fragment.GuidedRouter
import ru.radiationx.anilibria.screen.AuthGuidedScreen
import ru.radiationx.anilibria.screen.LifecycleViewModel
import toothpick.InjectConstructor
import tv.anilibria.feature.user.data.UserRepository
import tv.anilibria.feature.auth.data.AuthStateHolder
import tv.anilibria.feature.auth.data.AuthRepository

@InjectConstructor
class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val authStateHolder: AuthStateHolder,
    private val userRepository: UserRepository,
    private val guidedRouter: GuidedRouter
) : LifecycleViewModel() {

    val profileData = MutableLiveData<ProfileState>()

    override fun onCreate() {
        super.onCreate()

        userRepository
            .observeUser()
            .combine(authStateHolder.observe()) { user, authState ->
                ProfileState(user, authState)
            }
            .onEach {
                profileData.value = it
            }
            .launchIn(viewModelScope)
    }

    fun onSignInClick() {
        guidedRouter.open(AuthGuidedScreen())
    }

    fun onSignOutClick() {
        GlobalScope.launch {
            runCatching {
                authRepository.signOut()
            }
        }
    }
}