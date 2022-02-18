package ru.radiationx.anilibria.screen.mainpages

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.radiationx.anilibria.common.fragment.GuidedRouter
import ru.radiationx.anilibria.screen.LifecycleViewModel
import ru.radiationx.anilibria.screen.SearchScreen
import ru.radiationx.anilibria.screen.SuggestionsScreen
import ru.radiationx.anilibria.screen.UpdateScreen
import ru.terrakok.cicerone.Router
import toothpick.InjectConstructor
import tv.anilibria.feature.appupdates.data.CheckerRepository
import tv.anilibria.plugin.shared.appinfo.SharedBuildConfig

@InjectConstructor
class MainPagesViewModel(
    private val checkerRepository: CheckerRepository,
    private val buildConfig: SharedBuildConfig,
    private val guidedRouter: GuidedRouter,
    private val router: Router
) : LifecycleViewModel() {

    val hasUpdatesData = MutableLiveData<Boolean>()

    override fun onCreate() {
        super.onCreate()
        viewModelScope.launch {
            runCatching {
                checkerRepository.checkUpdate(buildConfig.versionCode, true)
            }.onSuccess {
                hasUpdatesData.value = it.code > buildConfig.versionCode
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    fun onAppUpdateClick() {
        router.navigateTo(UpdateScreen())
    }

    fun onCatalogClick() {
        router.navigateTo(SearchScreen())
    }

    fun onSearchClick() {
        router.navigateTo(SuggestionsScreen())
    }
}