package ru.radiationx.data

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.radiationx.data.adb.AppDatabase
import ru.radiationx.data.adb.converters.FeedConverter
import ru.radiationx.data.adb.dao.FeedDao
import ru.radiationx.data.adb.dao.ReleaseDao
import ru.radiationx.data.adb.dao.YoutubeDao
import ru.radiationx.data.adb.entity.feed.FeedDb
import ru.radiationx.data.adb.entity.release.BlockInfoDb
import ru.radiationx.data.adb.entity.release.FavoriteInfoDb
import ru.radiationx.data.adb.entity.release.FlatReleaseDb
import ru.radiationx.data.adb.entity.release.ReleaseDb
import ru.radiationx.data.adb.entity.youtube.YoutubeDb
import anilibria.tv.domain.entity.relative.FeedRelative
import anilibria.tv.domain.entity.release.Release
import java.util.*

@RunWith(AndroidJUnit4::class)
class SimpleDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var feedDao: FeedDao
    private lateinit var releaseDao: ReleaseDao
    private lateinit var youtubeDao: YoutubeDao

    @Before
    fun createDb() {
        val real = Room.databaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java,
            "kekos.db"
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    if (!db.isReadOnly) {
                        // Enable foreign key constraints
                        db.execSQL("PRAGMA foreign_keys=ON;");
                    }
                }
            })
            .build()

        val memory = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    if (!db.isReadOnly) {
                        // Enable foreign key constraints
                        db.execSQL("PRAGMA foreign_keys=ON;");
                    }
                }
            })
            .build()
        db = memory
        feedDao = db.feedDao()
        releaseDao = db.releaseDao()
        youtubeDao = db.youtubeDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun checkInsert() {

        val flatReleaseDb = FlatReleaseDb(
            10,
            "ten",
            "kek",
            "lol",
            "10",
            "poster",
            Date(60 * 1000),
            "web",
            Release.Status.COMPLETE,
            "type",
            listOf("genre"),
            listOf("voice"),
            "2010",
            1,
            "description",
            "announce",
            false
        )
        val favoriteDb = FavoriteInfoDb(10, 999, false)
        val blockInfoDb = BlockInfoDb(10, false, null)
        val releaseDb = ReleaseDb(flatReleaseDb, favoriteDb, blockInfoDb)

        val youTubeDb = YoutubeDb(20, "title", "image", "vid", 999, 888, Date(60 * 1000))
        val feedConverter = FeedConverter()

        val feedDb = feedConverter.toDb(
            FeedRelative(
                releaseDb.release.id,
                youTubeDb.id
            )
        )
        feedDao.insert(listOf(feedDb)).test().assertComplete()
        //feedDao.insert(listOf(feedDb), releaseDao, youtubeDao).test().assertComplete()
        assertEquals(1, feedDao.getList().blockingGet().size)
        assertEquals(1, releaseDao.getListAll().blockingGet().size)
        assertEquals(1, youtubeDao.getListAll().blockingGet().size)
        //val fetchedFeedDb = feedDao.getOneByRelease(10).blockingGet()
        //val fetchedFeedDb = feedDao.getOneByYoutube(20).blockingGet()
        //feedDao.delete(listOf(fetchedFeedDb.feed)).test().assertComplete()
        //youtubeDao.delete(listOf(youTubeDb)).test().assertComplete()
        youtubeDao.insert(listOf(youTubeDb.copy(title = "tuitle"))).test().assertComplete()
        assertEquals("tuitle", youtubeDao.getOne(20).blockingGet().title)
        assertEquals(1, feedDao.getList().blockingGet().size)
        assertEquals(1, releaseDao.getListAll().blockingGet().size)
        assertEquals(1, youtubeDao.getListAll().blockingGet().size)
    }
}