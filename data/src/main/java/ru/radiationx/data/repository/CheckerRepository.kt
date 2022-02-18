package ru.radiationx.data.repository

import android.util.Log
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.Single
import ru.radiationx.shared.ktx.SchedulersProvider
import ru.radiationx.data.datasource.remote.api.CheckerApi
import ru.radiationx.data.entity.app.updater.UpdateData
import javax.inject.Inject

/**
 * Created by radiationx on 28.01.18.
 */
@Deprecated("old data")
class CheckerRepository @Inject constructor(
    private val schedulers: SchedulersProvider,
    private val checkerApi: CheckerApi
) {


}
