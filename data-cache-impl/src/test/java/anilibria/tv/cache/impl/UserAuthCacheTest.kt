package anilibria.tv.cache.impl

import anilibria.tv.cache.impl.memory.UserAuthMemoryDataSource
import anilibria.tv.domain.entity.auth.UserAuth
import anilibria.tv.domain.entity.common.keys.EpisodeKey
import anilibria.tv.domain.entity.episode.Episode
import anilibria.tv.domain.entity.user.User
import anilibria.tv.storage.UserAuthStorageDataSource
import io.mockk.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Test

class UserAuthCacheTest {

    private val storageDataSource = mockk<UserAuthStorageDataSource>()
    private val memoryDataSource = mockk<UserAuthMemoryDataSource>()
    private val cache = UserAuthCacheImpl(storageDataSource, memoryDataSource)

    private val authData = UserAuth(UserAuth.State.AUTH, mockk())
    private val noAuthData = UserAuth(UserAuth.State.NO_AUTH, null)

    @Test
    fun `observe EXPECT call observe memory`() {
        every { memoryDataSource.observeData() } returns Observable.just(authData)

        cache.observe().test().assertValue(authData)

        verify { memoryDataSource.observeData() }
        confirmVerified(storageDataSource, memoryDataSource)
    }

    @Test
    fun `get WHEN memory empty EXPECT get from storage, update memory`() {
        val insertSlot = slot<UserAuth>()

        every { memoryDataSource.getData() } returns Maybe.fromCallable {
            if (insertSlot.isCaptured) insertSlot.captured else null
        }
        every { storageDataSource.getUser() } returns Single.just(authData)
        every { memoryDataSource.insert(capture(insertSlot)) } returns Completable.complete()

        cache.get().test().assertValue(authData)

        verify { memoryDataSource.getData() }
        verify { storageDataSource.getUser() }
        verify { memoryDataSource.insert(authData) }
        confirmVerified(storageDataSource, memoryDataSource)
    }

    @Test
    fun `get WHEN memory not empty EXPECT get from memory`() {
        every { memoryDataSource.getData() } returns Maybe.fromCallable { authData }

        cache.get().test().assertValue(authData)

        verify { memoryDataSource.getData() }
        confirmVerified(storageDataSource, memoryDataSource)
    }

    @Test
    fun `save EXPECT success save`() {
        val user = authData.user!!

        every { storageDataSource.putUser(user) } returns Completable.complete()
        every { storageDataSource.getUser() } returns Single.just(authData)
        every { memoryDataSource.insert(authData) } returns Completable.complete()

        cache.save(user).test().assertComplete()

        verify { storageDataSource.putUser(user) }
        verify { storageDataSource.getUser() }
        verify { memoryDataSource.insert(authData) }
        confirmVerified(storageDataSource, memoryDataSource)
    }

    @Test
    fun `clear EXPECT success clear`() {
        every { storageDataSource.delete() } returns Completable.complete()
        every { storageDataSource.getUser() } returns Single.just(noAuthData)
        every { memoryDataSource.insert(noAuthData) } returns Completable.complete()

        cache.clear().test().assertComplete()

        verify { storageDataSource.delete() }
        verify { storageDataSource.getUser() }
        verify { memoryDataSource.insert(noAuthData) }
        confirmVerified(storageDataSource, memoryDataSource)

    }
}