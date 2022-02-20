package ru.radiationx.anilibria.ui.common

import ru.radiationx.anilibria.presentation.common.IErrorHandler
import ru.radiationx.anilibria.utils.messages.SystemMessenger
import java.io.IOException
import javax.inject.Inject

/**
 * Created by radiationx on 23.02.18.
 */
// todo обновить логику обрбаотки ошибок
class ErrorHandler @Inject constructor(
    private val systemMessenger: SystemMessenger
) : IErrorHandler {

    override fun handle(throwable: Throwable, messageListener: ((Throwable, String?) -> Unit)?) {
        throwable.printStackTrace()
        val message = getMessage(throwable)
        if (messageListener != null) {
            messageListener.invoke(throwable, message)
        } else {
            systemMessenger.showMessage(message)
        }
    }

    private fun getMessage(throwable: Throwable) = when (throwable) {
        is IOException -> "Нет соединения с интернетом"
        // todo заменить на класс от ретрофита
        //is HttpException -> throwable.message
        //is ApiError -> throwable.userMessage()
        else -> throwable.message.orEmpty()
    }

    /*private fun ApiError.userMessage() = when {
        !message.isNullOrBlank() -> message.orEmpty()
        !description.isNullOrBlank() -> description.orEmpty()
        else -> "Неизвестная ошибка"
    }*/
}