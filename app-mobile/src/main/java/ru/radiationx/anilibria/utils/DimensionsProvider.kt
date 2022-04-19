package ru.radiationx.anilibria.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import toothpick.InjectConstructor

@InjectConstructor
class DimensionsProvider {

    private val relay = MutableStateFlow(DimensionHelper.Dimensions())

    fun observe(): StateFlow<DimensionHelper.Dimensions> = relay

    fun get() = relay.value

    fun update(dimensions: DimensionHelper.Dimensions) {
        relay.value = dimensions
    }
}