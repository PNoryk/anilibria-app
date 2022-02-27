package ru.radiationx.anilibria.screen.auth.credentials

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.radiationx.anilibria.common.fragment.GuidedRouter
import ru.radiationx.anilibria.screen.LifecycleViewModel
import ru.terrakok.cicerone.Router
import toothpick.InjectConstructor
import tv.anilibria.feature.auth.data.AuthRepository

@InjectConstructor
class AuthCredentialsViewModel(
    private val authRepository: AuthRepository,
    private val router: Router,
    private val guidedRouter: GuidedRouter
) : LifecycleViewModel() {

    val progressState = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    fun onLoginClicked(login: String, password: String, code: String) {
        progressState.value = true
        error.value = ""

        viewModelScope.launch {
            runCatching {
                authRepository.signIn(login, password, code)
            }.onSuccess {
                progressState.value = false
                guidedRouter.finishGuidedChain()
            }.onFailure {
                progressState.value = false
                it.printStackTrace()
                error.value = it.message
            }
        }

    }
}