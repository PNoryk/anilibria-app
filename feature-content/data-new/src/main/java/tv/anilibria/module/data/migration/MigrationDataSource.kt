package tv.anilibria.module.data.migration

interface MigrationDataSource {
    fun getHistory(): List<Int>
    fun update()
}