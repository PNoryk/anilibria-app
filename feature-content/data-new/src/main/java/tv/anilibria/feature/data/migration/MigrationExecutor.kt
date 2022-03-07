package tv.anilibria.feature.data.migration

interface MigrationExecutor {
    fun execute(current: Int, lastSaved: Int, history: List<Int>)
}