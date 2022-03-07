package ru.radiationx.anilibria

import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.migration.MigrationExecutor

@InjectConstructor
class AppMigrationExecutor : MigrationExecutor {

    override fun execute(current: Int, lastSaved: Int, history: List<Int>) {
    }
}