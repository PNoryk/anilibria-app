package ru.radiationx.anilibria.ui.common

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import ru.radiationx.anilibria.utils.messages.SystemMessage
import ru.radiationx.anilibria.utils.messages.SystemMessenger
import toothpick.InjectConstructor

@InjectConstructor
class ScreenMessagesObserver(
    private val context: Context,
    private val screenMessenger: SystemMessenger
) : LifecycleObserver {

    private val scope = CoroutineScope(Dispatchers.Main)
    private var messengerJob: Job? = null
    private val messageBufferTrigger = MutableStateFlow(false)
    private val messagesBuffer = mutableListOf<SystemMessage>()

    init {
        screenMessenger
            .observe()
            .onEach {
                messagesBuffer.add(it)
                messageBufferTrigger.value = true
            }
            .launchIn(scope)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun resume() {
        messengerJob?.cancel()
        messengerJob = messageBufferTrigger
            .filter { it }
            .flatMapConcat {
                val messages = messagesBuffer.toList()
                flow {
                    messages.forEach { emit(it) }
                }
            }
            .distinctUntilChanged()
            .onEach { message ->
                showMessage(message)
                messagesBuffer.clear()
            }
            .launchIn(scope)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pause() {
        messengerJob?.cancel()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        scope.cancel()
    }

    private fun showMessage(message: SystemMessage) {
        context.also {
            Toast.makeText(it, message.message, Toast.LENGTH_SHORT).show()
        }
    }
}