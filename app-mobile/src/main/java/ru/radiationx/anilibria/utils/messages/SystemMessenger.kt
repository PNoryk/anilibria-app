package ru.radiationx.anilibria.utils.messages

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SystemMessenger @Inject constructor() {
    private val messagesRelay = MutableSharedFlow<SystemMessage>()

    fun observe(): SharedFlow<SystemMessage> = messagesRelay.asSharedFlow()

    fun showMessage(message: String) {
        GlobalScope.launch {
            messagesRelay.emit(SystemMessage(message))
        }
    }
}