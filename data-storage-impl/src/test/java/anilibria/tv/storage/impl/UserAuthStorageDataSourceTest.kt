package anilibria.tv.storage.impl

import anilibria.tv.domain.entity.auth.UserAuth
import anilibria.tv.domain.entity.user.User
import anilibria.tv.storage.common.KeyValueStorage
import anilibria.tv.storage.impl.converter.UserAuthConverter
import anilibria.tv.storage.impl.converter.UserConverter
import com.google.gson.Gson
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Maybe
import org.junit.Test

class UserAuthStorageDataSourceTest {

    private val KEY = "saved_user"
    private val gson = Gson()
    private val userConverter = UserConverter()
    private val converter = UserAuthConverter(userConverter)
    private val keyValueStorage = mockk<KeyValueStorage>()
    private val dataSource = UserAuthStorageDataSourceImpl(keyValueStorage, gson, converter)

    private val defaultUser = UserAuth(
        state = UserAuth.State.NO_AUTH,
        user = null
    )

    private val user = User(
        id = 1,
        login = "Login",
        avatar = "avatar"
    )

    private val userAuth = UserAuth(
        state = UserAuth.State.AUTH,
        user = user
    )

    private val json = """{"state":2,"user":{"id":1,"login":"Login","avatar":"avatar"}}"""

    @Test
    fun `get when no data EXPECT default user`() {
        every { keyValueStorage.getValue(KEY) } returns Maybe.fromCallable { null }

        dataSource.getUser().test().assertValue(defaultUser)

        verify { keyValueStorage.getValue(KEY) }
        confirmVerified(keyValueStorage)
    }

    @Test
    fun `put user EXPECT success`() {
        every { keyValueStorage.putValue(KEY, any()) } returns Completable.complete()

        dataSource.putUser(user).test().assertComplete()

        verify { keyValueStorage.putValue(KEY, json) }
        confirmVerified(keyValueStorage)
    }

    @Test
    fun `get saved EXPECT saved user`() {
        every { keyValueStorage.getValue(KEY) } returns Maybe.fromCallable { json }

        dataSource.getUser().test().assertValue(userAuth)

        verify { keyValueStorage.getValue(KEY) }
        confirmVerified(keyValueStorage)
    }

    @Test
    fun `delete user EXPECT success`() {
        every { keyValueStorage.delete(KEY) } returns Completable.complete()

        dataSource.delete().test().assertComplete()

        verify { keyValueStorage.delete(KEY) }
        confirmVerified(keyValueStorage)
    }
}