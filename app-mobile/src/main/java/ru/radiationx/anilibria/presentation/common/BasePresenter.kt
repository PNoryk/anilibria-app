package ru.radiationx.anilibria.presentation.common

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
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

    private var compositeDisposable = CompositeDisposable()

    val viewModelScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onDestroy() {
        viewModelScope.cancel()
        compositeDisposable.dispose()
    }

    fun <T : Disposable> T.addToDisposable(): T {
        compositeDisposable.add(this)
        return this
    }

    fun onBackPressed() {
        router.exit()
    }
}
