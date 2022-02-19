package ru.radiationx.anilibria.ui.fragments.favorites

import ru.radiationx.anilibria.model.ReleaseItemState
import ru.radiationx.anilibria.model.loading.DataLoadingState
import tv.anilibria.module.domain.entity.release.ReleaseId

data class FavoritesScreenState(
    val searchItems: List<ReleaseItemState> = emptyList(),
    val deletingItemIds: List<ReleaseId> = emptyList(),
    val data: DataLoadingState<List<ReleaseItemState>> = DataLoadingState()
)