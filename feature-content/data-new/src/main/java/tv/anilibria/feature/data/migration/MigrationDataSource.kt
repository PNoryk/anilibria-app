package tv.anilibria.feature.data.migration

interface MigrationDataSource {
    fun getHistory(): List<Int>
    fun update()
}