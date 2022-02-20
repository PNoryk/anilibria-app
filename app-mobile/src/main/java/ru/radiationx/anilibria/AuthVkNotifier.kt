package ru.radiationx.anilibria

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by radiationx on 30.12.17.
 */
class AuthVkNotifier @Inject constructor() {

    private val vkAuthRelay = PublishRelay.create<Boolean>()

    fun observeVkAuthChange(): Observable<Boolean> = vkAuthRelay.hide()

    fun changeVkAuth(value: Boolean) {
        vkAuthRelay.accept(value)
    }
}