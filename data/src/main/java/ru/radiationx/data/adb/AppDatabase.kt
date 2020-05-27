package ru.radiationx.data.adb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.radiationx.data.adb.dao.*
import ru.radiationx.data.adb.entity.episode.EpisodeDb
import ru.radiationx.data.adb.entity.favorite.FavoriteDb
import ru.radiationx.data.adb.entity.feed.FeedDb
import ru.radiationx.data.adb.entity.history.HistoryDb
import ru.radiationx.data.adb.entity.release.BlockInfoDb
import ru.radiationx.data.adb.entity.release.FavoriteInfoDb
import ru.radiationx.data.adb.entity.release.FlatReleaseDb
import ru.radiationx.data.adb.entity.schedule.FlatScheduleDayDb
import ru.radiationx.data.adb.entity.schedule.ScheduleReleaseDb
import ru.radiationx.data.adb.entity.torrent.TorrentDb
import ru.radiationx.data.adb.entity.youtube.YoutubeDb

@Database(
    entities = [
        EpisodeDb::class,

        FavoriteDb::class,

        FeedDb::class,

        HistoryDb::class,

        BlockInfoDb::class,
        FavoriteInfoDb::class,
        FlatReleaseDb::class,

        FlatScheduleDayDb::class,
        ScheduleReleaseDb::class,

        TorrentDb::class,

        YoutubeDb::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun episodeDao(): EpisodeDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun feedDao(): FeedDao
    abstract fun historyDao(): HistoryDao
    abstract fun releaseDao(): ReleaseDao
    abstract fun scheduleDao(): ScheduleDao
    abstract fun torrentDao(): TorrentDao
    abstract fun youtubeDao(): YoutubeDao
}