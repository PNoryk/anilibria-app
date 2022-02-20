package ru.radiationx.anilibria.presentation.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import moxy.MvpPresenter
import moxy.MvpView
import ru.terrakok.cicerone.Router

/**
 * Created by radiationx on 05.11.17.
 */


val MvpPresenter<*>.viewModelScope
    get() = CoroutineScope(Dispatchers.Main)

open class BasePresenter<ViewT : MvpView>(private val router: Router) : MvpPresenter<ViewT>() {

    val viewModelScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onDestroy() {
        viewModelScope.cancel()
    }

    fun onBackPressed() {
        router.exit()
    }
}
