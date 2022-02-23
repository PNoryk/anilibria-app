package ru.radiationx.anilibria.screen.suggestions

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.radiationx.anilibria.common.LibriaCard
import ru.radiationx.anilibria.screen.DetailsScreen
import ru.radiationx.anilibria.screen.LifecycleViewModel
import ru.terrakok.cicerone.Router
import toothpick.InjectConstructor
import tv.anilibria.module.data.repos.SearchRepository
import tv.anilibria.module.domain.entity.release.Release
import tv.anilibria.module.domain.entity.release.ReleaseId
import kotlin.time.Duration.Companion.milliseconds

@InjectConstructor
class SuggestionsResultViewModel(
    private val searchRepository: SearchRepository,
    private val router: Router,
    private val suggestionsController: SuggestionsController
) : LifecycleViewModel() {

    private var currentQuery = ""
    private var queryRelay = MutableSharedFlow<String>()

    val progressState = MutableLiveData<Boolean>()
    val resultData = MutableLiveData<List<LibriaCard>>()

    override fun onColdCreate() {
        super.onColdCreate()

        queryRelay
            .debounce(350L.milliseconds)
            .distinctUntilChanged()
            .onEach {
                if (it.length < 3) {
                    showItems(emptyList(), it, false)
                }
            }
            .filter { it.length >= 3 }
            .onEach { progressState.value = true }
            .mapLatest { query ->
                runCatching {
                    searchRepository.fastSearch(query)
                }.getOrNull() ?: emptyList()
            }
            .onEach {
                showItems(it, currentQuery, true)
            }
            .catch {
                it.printStackTrace()
            }
            .launchIn(viewModelScope)
    }

    fun onQueryChange(query: String) {
        currentQuery = query
        viewModelScope.launch {
            queryRelay.emit(currentQuery)
        }
    }

    fun onCardClick(item: LibriaCard) {
        router.navigateTo(DetailsScreen(ReleaseId(item.id)))
    }

    private fun showItems(items: List<Release>, query: String, validQuery: Boolean) {
        viewModelScope.launch {
            val result = SuggestionsController.SearchResult(items, query, validQuery)
            suggestionsController.resultEvent.emit(result)
        }
        progressState.value = false
        resultData.value = items.map {
            LibriaCard(
                it.id.id,
                it.nameRus?.text.orEmpty(),
                it.nameEng?.text.orEmpty(),
                it.poster?.value.orEmpty(),
                LibriaCard.Type.RELEASE
            )
        }
    }
}