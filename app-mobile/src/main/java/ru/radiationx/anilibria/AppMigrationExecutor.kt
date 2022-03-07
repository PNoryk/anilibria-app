package ru.radiationx.anilibria

import com.nostra13.universalimageloader.core.ImageLoader
import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.migration.MigrationExecutor
import tv.anilibria.app.mobile.preferences.PreferencesStorage

@InjectConstructor
class AppMigrationExecutor(
    private val appPreferences: PreferencesStorage
) : MigrationExecutor {


    override fun execute(current: Int, lastSaved: Int, history: List<Int>) {
        if (current == 20) {
            appPreferences.releaseRemind.blockingSet(true)
        }
        if (lastSaved <= 52) {
            ImageLoader.getInstance().clearDiskCache()
        }
    }
}