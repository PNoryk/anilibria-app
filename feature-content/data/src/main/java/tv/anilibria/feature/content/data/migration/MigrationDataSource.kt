package tv.anilibria.feature.content.data.migration

interface MigrationDataSource {
    suspend fun getHistory(): List<Int>
    suspend fun update()
}