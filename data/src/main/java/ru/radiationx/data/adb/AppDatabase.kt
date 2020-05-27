package ru.radiationx.data.adb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.radiationx.data.adb.dao.*
import ru.radiationx.data.adb.episode.EpisodeDb
import ru.radiationx.data.adb.favorite.FavoriteDb
import ru.radiationx.data.adb.feed.FeedDb
import ru.radiationx.data.adb.history.HistoryDb
import ru.radiationx.data.adb.release.BlockInfoDb
import ru.radiationx.data.adb.release.FavoriteInfoDb
import ru.radiationx.data.adb.release.FlatReleaseDb
import ru.radiationx.data.adb.schedule.FlatScheduleDayDb
import ru.radiationx.data.adb.schedule.ScheduleReleaseDb
import ru.radiationx.data.adb.torrent.TorrentDb
import ru.radiationx.data.adb.youtube.YoutubeDb

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