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
import anilibria.tv.db.impl.AppDatabase
import anilibria.tv.db.impl.converters.FeedConverter
import anilibria.tv.db.impl.dao.FeedDao
import anilibria.tv.db.impl.dao.ReleaseDao
import anilibria.tv.db.impl.dao.YoutubeDao
import anilibria.tv.db.impl.entity.feed.FeedDb
import anilibria.tv.db.impl.entity.release.BlockInfoDb
import anilibria.tv.db.impl.entity.release.FavoriteInfoDb
import anilibria.tv.db.impl.entity.release.FlatReleaseDb
import anilibria.tv.db.impl.entity.release.ReleaseDb
import anilibria.tv.db.impl.entity.youtube.YoutubeDb
import anilibria.tv.domain.entity.relative.FeedRelative
import anilibria.tv.domain.entity.release.Release
import java.util.*

@RunWith(AndroidJUnit4::class)
class SimpleDaoTest {
    private lateinit var db: anilibria.tv.db.impl.AppDatabase
    private lateinit var feedDao: FeedDao
    private lateinit var releaseDao: ReleaseDao
    private lateinit var youtubeDao: YoutubeDao

    @Before
    fun createDb() {
        val real = Room.databaseBuilder(
            ApplicationProvider.getApplicationContext(),
            anilibria.tv.db.impl.AppDatabase::class.java,
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
            anilibria.tv.db.impl.AppDatabase::class.java
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