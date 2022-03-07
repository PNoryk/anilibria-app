package tv.anilibria.feature.content.data.migration

interface MigrationDataSource {
    fun getHistory(): List<Int>
    fun update()
}