package ru.radiationx.data.system

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.radiationx.shared.ktx.SchedulersProvider
import javax.inject.Inject

@Deprecated("old data")
class AppSchedulers @Inject constructor() : SchedulersProvider {
    override fun ui(): Scheduler = AndroidSchedulers.mainThread()
    override fun computation(): Scheduler = Schedulers.computation()
    override fun trampoline(): Scheduler = Schedulers.trampoline()
    override fun newThread(): Scheduler = Schedulers.newThread()
    override fun io(): Scheduler = Schedulers.io()
}

