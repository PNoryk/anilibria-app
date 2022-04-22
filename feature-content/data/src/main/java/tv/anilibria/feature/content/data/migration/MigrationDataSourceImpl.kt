package tv.anilibria.feature.content.data.migration

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.di.MigrationStorageQualifier
import tv.anilibria.plugin.data.analytics.AnalyticsErrorReporter
import tv.anilibria.plugin.data.storage.DataStorage
import tv.anilibria.plugin.data.storage.storageStringKey
import tv.anilibria.plugin.shared.appinfo.SharedBuildConfig

@InjectConstructor
class MigrationDataSourceImpl(
    private val context: Context,
    @MigrationStorageQualifier private val storage: DataStorage,
    private val sharedBuildConfig: SharedBuildConfig,
    private val migrationExecutor: MigrationExecutor,
    private val errorReporter: AnalyticsErrorReporter
) : MigrationDataSource {

    companion object {
        private val PREF_KEY = storageStringKey("app.versions.history")
        private const val INITIAL_VERSION = 0
        private const val ANALYTIC_GROUP = "migration"
    }

    override suspend fun getHistory(): List<Int> {
        return storage
            .get(PREF_KEY)
            ?.split(";")
            ?.filter { it.isNotBlank() }
            ?.map { it.toInt() }
            ?: emptyList()
    }

    override suspend fun update() {
        try {
            val history = getHistory()
            val currentVersion = sharedBuildConfig.versionCode
            val lastVersion = history.lastOrNull() ?: INITIAL_VERSION
            val disorder = checkIsDisordered(history)

            if (lastVersion < currentVersion) {
                if (lastVersion > INITIAL_VERSION) {
                    migrationExecutor.execute(currentVersion, lastVersion, history)
                }
                val newHistory = history + currentVersion
                saveHistory(newHistory)
            }
            if (disorder) {
                val errMsg =
                    "AniLibria: Нарушение порядка версий, программа может работать не стабильно!"
                errorReporter.report(ANALYTIC_GROUP, errMsg)
                Toast.makeText(context, errMsg, Toast.LENGTH_SHORT).show()
            }
        } catch (ex: Throwable) {
            val errMsg = "Сбой при проверке локальной версии."
            Log.e("MigrationDataSource", errMsg, ex)
            errorReporter.report(ANALYTIC_GROUP, errMsg, ex)
            val uiErr = "$errMsg\nПрограмма может работать не стабильно! Переустановите программу."
            Toast.makeText(context, uiErr, Toast.LENGTH_LONG).show()
        }
    }

    private fun checkIsDisordered(history: List<Int>): Boolean {
        var prevVersion = 0
        history.forEach {
            if (it < prevVersion) {
                return true
            }
            prevVersion = it
        }
        return false
    }

    private suspend fun saveHistory(history: List<Int>) {
        storage.set(PREF_KEY, TextUtils.join(";", history.map { it.toString() }))
    }
}