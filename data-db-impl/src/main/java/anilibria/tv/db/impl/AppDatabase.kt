package anilibria.tv.db.impl

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import anilibria.tv.db.impl.dao.*
import anilibria.tv.db.impl.entity.episode.EpisodeDb
import anilibria.tv.db.impl.entity.favorite.FavoriteDb
import anilibria.tv.db.impl.entity.feed.FeedDb
import anilibria.tv.db.impl.entity.history.EpisodeHistoryDb
import anilibria.tv.db.impl.entity.history.ReleaseHistoryDb
import anilibria.tv.db.impl.entity.release.BlockInfoDb
import anilibria.tv.db.impl.entity.release.FavoriteInfoDb
import anilibria.tv.db.impl.entity.release.FlatReleaseDb
import anilibria.tv.db.impl.entity.schedule.FlatScheduleDayDb
import anilibria.tv.db.impl.entity.schedule.ScheduleReleaseDb
import anilibria.tv.db.impl.entity.torrent.TorrentDb
import anilibria.tv.db.impl.entity.youtube.YoutubeDb

@Database(
    entities = [
        EpisodeDb::class,

        FavoriteDb::class,

        FeedDb::class,

        EpisodeHistoryDb::class,
        ReleaseHistoryDb::class,

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
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun episodeDao(): EpisodeDao
    abstract fun episodeHistoryDao(): EpisodeHistoryDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun feedDao(): FeedDao
    abstract fun releaseDao(): ReleaseDao
    abstract fun releaseHistoryDao(): ReleaseHistoryDao
    abstract fun scheduleDao(): ScheduleDao
    abstract fun torrentDao(): TorrentDao
    abstract fun youtubeDao(): YoutubeDao
}