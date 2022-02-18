package ru.radiationx.anilibria.screen.update.source

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.radiationx.anilibria.common.fragment.GuidedRouter
import ru.radiationx.anilibria.screen.LifecycleViewModel
import ru.radiationx.anilibria.screen.update.UpdateController
import ru.radiationx.shared_app.common.SystemUtils
import toothpick.InjectConstructor
import tv.anilibria.feature.appupdates.data.CheckerRepository
import tv.anilibria.feature.appupdates.data.domain.UpdateLink
import tv.anilibria.feature.appupdates.data.domain.UpdateLinkType

@InjectConstructor
class UpdateSourceViewModel(
    private val checkerRepository: CheckerRepository,
    private val guidedRouter: GuidedRouter,
    private val systemUtils: SystemUtils,
    private val updateController: UpdateController
) : LifecycleViewModel() {

    val sourcesData = MutableLiveData<List<UpdateLink>>()

    override fun onCreate() {
        super.onCreate()

        checkerRepository
            .observeUpdate()
            .onEach {
                sourcesData.value = it.links
            }
            .catch { it.printStackTrace() }
            .launchIn(viewModelScope)
    }

    fun onLinkClick(index: Int) {
        val link = sourcesData.value?.getOrNull(index) ?: return
        viewModelScope.launch {
            when (link.type) {
                UpdateLinkType.FILE -> updateController.downloadAction.emit(link)
                UpdateLinkType.SITE -> systemUtils.externalLink(link.url.value)
                UpdateLinkType.UNKNOWN -> systemUtils.externalLink(link.url.value)
            }
            guidedRouter.close()
        }
    }
}