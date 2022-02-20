package ru.radiationx.anilibria

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

/**
 * Created by radiationx on 30.12.17.
 */
class AuthVkNotifier @Inject constructor() {

    private val vkAuthRelay = MutableSharedFlow<Boolean>()

    fun observeVkAuthChange(): SharedFlow<Boolean> = vkAuthRelay.asSharedFlow()

    suspend fun changeVkAuth(value: Boolean) {
        vkAuthRelay.emit(value)
    }
}