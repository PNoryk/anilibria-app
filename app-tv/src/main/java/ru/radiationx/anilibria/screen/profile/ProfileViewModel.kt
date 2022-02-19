package ru.radiationx.anilibria.screen.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.radiationx.anilibria.common.fragment.GuidedRouter
import ru.radiationx.anilibria.screen.AuthGuidedScreen
import ru.radiationx.anilibria.screen.LifecycleViewModel
import toothpick.InjectConstructor
import tv.anilibria.module.data.repos.AuthRepository
import tv.anilibria.module.data.repos.UserRepository
import tv.anilibria.module.domain.entity.other.User

@InjectConstructor
class ProfileViewModel(
    private val authRepositoryNew: AuthRepository,
    private val userRepository: UserRepository,
    private val guidedRouter: GuidedRouter
) : LifecycleViewModel() {

    val profileData = MutableLiveData<User>()

    override fun onCreate() {
        super.onCreate()

        userRepository
            .observeUser()
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
                authRepositoryNew.signOut()
            }
        }
    }
}