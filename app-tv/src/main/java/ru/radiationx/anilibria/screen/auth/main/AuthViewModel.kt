package ru.radiationx.anilibria.screen.auth.main

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.radiationx.anilibria.common.fragment.GuidedRouter
import ru.radiationx.anilibria.screen.AuthCredentialsGuidedScreen
import ru.radiationx.anilibria.screen.AuthOtpGuidedScreen
import ru.radiationx.anilibria.screen.LifecycleViewModel
import toothpick.InjectConstructor
import tv.anilibria.module.data.AuthStateHolder

@InjectConstructor
class AuthViewModel(
    private val authStateHolder: AuthStateHolder,
    private val guidedRouter: GuidedRouter
) : LifecycleViewModel() {

    fun onCodeClick() {
        guidedRouter.open(AuthOtpGuidedScreen())
    }

    fun onClassicClick() {
        guidedRouter.open(AuthCredentialsGuidedScreen())
    }

    fun onSocialClick() {

    }

    fun onSkipClick() {
        viewModelScope.launch {
            authStateHolder.skip()
            guidedRouter.finishGuidedChain()
        }
    }
}