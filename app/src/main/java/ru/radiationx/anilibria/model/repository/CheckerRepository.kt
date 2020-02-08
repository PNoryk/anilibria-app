package ru.radiationx.anilibria.model.repository

import android.util.Log
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Single
import ru.radiationx.data.entity.app.updater.UpdateData
import ru.radiationx.anilibria.model.datasource.remote.api.CheckerApi
import ru.radiationx.anilibria.model.system.SchedulersProvider
import javax.inject.Inject

/**
 * Created by radiationx on 28.01.18.
 */
class CheckerRepository @Inject constructor(
        private val schedulers: SchedulersProvider,
        private val checkerApi: CheckerApi
) {

    private val currentDataRelay = BehaviorRelay.create<UpdateData>()

    fun checkUpdate(versionCode: Int, force: Boolean = false): Single<UpdateData> = Single
            .fromCallable {
                Log.e("CHECKER", "fromCallable0 $versionCode : $force")
                return@fromCallable if (!force && currentDataRelay.hasValue())
                    currentDataRelay.value!!
                else
                    checkerApi.checkUpdate(versionCode).blockingGet()
            }
            .doOnSuccess {
                Log.e("CHECKER", "doOnSuccess $it")
                currentDataRelay.accept(it)
            }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

}
