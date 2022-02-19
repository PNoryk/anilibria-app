package ru.radiationx.anilibria.screen.launcher

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.radiationx.anilibria.screen.*
import ru.radiationx.data.datasource.remote.address.ApiConfig
import ru.radiationx.shared.ktx.SchedulersProvider
import ru.terrakok.cicerone.Router
import toothpick.InjectConstructor
import tv.anilibria.module.data.AuthStateHolder
import tv.anilibria.module.domain.entity.AuthState
import tv.anilibria.module.domain.entity.release.ReleaseId

@InjectConstructor
class AppLauncherViewModel(
    private val apiConfig: ApiConfig,
    private val schedulersProvider: SchedulersProvider,
    private val router: Router,
    private val authStateHolder: AuthStateHolder
) : LifecycleViewModel() {

    private var firstLaunch = true

    val appReadyAction = MutableLiveData<Unit>()

    fun openRelease(id: ReleaseId) {
        router.navigateTo(DetailsScreen(id))
    }

    fun coldLaunch() {
        initWithConfig()
        //initMain()
    }

    private fun initWithConfig() {
        apiConfig
            .observeNeedConfig()
            .distinctUntilChanged()
            .observeOn(schedulersProvider.ui())
            .lifeSubscribe {
                if (it) {
                    router.newRootScreen(ConfigScreen())
                } else {
                    if (firstLaunch) {
                        initMain()
                    }
                }
            }

        if (apiConfig.needConfig) {
            router.newRootScreen(ConfigScreen())
        } else {
            initMain()
        }
    }

    private fun initMain() {
        firstLaunch = false
        router.newRootScreen(MainPagesScreen())
        //router.newRootScreen(SearchScreen())
        viewModelScope.launch {
            if (authStateHolder.get() == AuthState.NO_AUTH) {
                router.navigateTo(AuthGuidedScreen())
            }
        }
        appReadyAction.value = Unit
    }

}