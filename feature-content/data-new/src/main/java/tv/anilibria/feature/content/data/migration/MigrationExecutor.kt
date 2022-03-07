package tv.anilibria.feature.content.data.migration

interface MigrationExecutor {
    fun execute(current: Int, lastSaved: Int, history: List<Int>)
}