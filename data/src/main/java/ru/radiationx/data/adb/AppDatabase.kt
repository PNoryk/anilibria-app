package ru.radiationx.data.adb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.radiationx.data.adb.dao.*
import ru.radiationx.data.adb.episode.EpisodeDb
import ru.radiationx.data.adb.favorite.FlatFavoriteDb
import ru.radiationx.data.adb.feed.FlatFeedDb
import ru.radiationx.data.adb.history.FlatHistoryDb
import ru.radiationx.data.adb.release.BlockInfoDb
import ru.radiationx.data.adb.release.FavoriteInfoDb
import ru.radiationx.data.adb.release.FlatReleaseDb
import ru.radiationx.data.adb.schedule.FlatScheduleDayDb
import ru.radiationx.data.adb.schedule.ScheduleReleaseCrossRefDb
import ru.radiationx.data.adb.torrent.TorrentDb
import ru.radiationx.data.adb.youtube.YouTubeDb

@Database(
    entities = [
        EpisodeDb::class,

        FlatFavoriteDb::class,

        FlatFeedDb::class,

        FlatHistoryDb::class,

        BlockInfoDb::class,
        FavoriteInfoDb::class,
        FlatReleaseDb::class,

        FlatScheduleDayDb::class,
        ScheduleReleaseCrossRefDb::class,

        TorrentDb::class,

        YouTubeDb::class
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
    abstract fun scheduleDao(): ScheduleDayDao
    abstract fun torrentDao(): TorrentDao
    abstract fun youtubeDao(): YoutubeDao
}