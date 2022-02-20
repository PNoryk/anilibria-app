package ru.radiationx.anilibria.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * Created by radiationx on 09.01.18.
 */
class DimensionsProvider @Inject constructor() {

    private val relay = MutableStateFlow(DimensionHelper.Dimensions())

    fun observe(): StateFlow<DimensionHelper.Dimensions> = relay

    fun get() = relay.value

    fun update(dimensions: DimensionHelper.Dimensions) {
        relay.value = dimensions
    }
}