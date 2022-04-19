package tv.anilibria.feature.appupdates.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import toothpick.InjectConstructor
import tv.anilibria.feature.appupdates.data.domain.UpdateData
import tv.anilibria.plugin.data.storage.ObservableData

@InjectConstructor
class CheckerRepository(
    private val checkerApi: UpdatesRemoteDataSource
) {

    private val observableData = ObservableData<UpdateData>()

    fun observeUpdate(): Flow<UpdateData> = observableData.observe().filterNotNull()

    suspend fun checkUpdate(versionCode: Int, force: Boolean = false): UpdateData {
        return observableData.get()
            ?.takeIf { !force }
            ?: checkerApi.checkUpdate(versionCode).also {
                observableData.put(it)
            }
    }
}
