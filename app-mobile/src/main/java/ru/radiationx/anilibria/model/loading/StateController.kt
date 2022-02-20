package ru.radiationx.anilibria.model.loading

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StateController<T : Any>(defaultState: T) {

    private val stateRelay = MutableStateFlow(defaultState)

    val currentState: T
        get() = stateRelay.value

    fun updateState(block: (T) -> T) {
        stateRelay.update(block)
    }

    fun observeState(): StateFlow<T> {
        return stateRelay.asStateFlow()
    }
}