package ru.radiationx.anilibria

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import toothpick.InjectConstructor
import javax.inject.Inject

@InjectConstructor
class AuthVkNotifier {

    private val vkAuthRelay = MutableSharedFlow<Boolean>()

    fun observeVkAuthChange(): SharedFlow<Boolean> = vkAuthRelay.asSharedFlow()

    suspend fun changeVkAuth(value: Boolean) {
        vkAuthRelay.emit(value)
    }
}