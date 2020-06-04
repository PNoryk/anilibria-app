package anilibria.tv.storage.impl.converter

import anilibria.tv.domain.entity.auth.SocialService
import anilibria.tv.domain.entity.auth.UserAuth
import anilibria.tv.domain.entity.user.User
import anilibria.tv.storage.impl.entity.SocialServiceStorage
import anilibria.tv.storage.impl.entity.UserAuthStorage
import anilibria.tv.storage.impl.entity.UserStorage
import org.junit.Assert
import org.junit.Test

class UserAuthConverterTest {

    private val converter = UserAuthConverter(UserConverter())

    private val userDomain = User(
        id = 1,
        login = "Login",
        avatar = "avatar"
    )

    private val userStorage = UserStorage(
        id = 1,
        login = "Login",
        avatar = "avatar"
    )

    @Test
    fun `item from storage EXPECT domain item`() {
        val source = UserAuthStorage(UserAuthStorage.STATE_AUTH, userStorage)
        val expected = UserAuth(UserAuth.State.AUTH, userDomain)
        val actual = converter.toDomain(source)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `item from domain EXPECT storage item`() {
        val source = UserAuth(UserAuth.State.AUTH, userDomain)
        val expected = UserAuthStorage(UserAuthStorage.STATE_AUTH, userStorage)
        val actual = converter.toStorage(source)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `no auth item from storage EXPECT domain no auth item`() {
        val source = UserAuthStorage(UserAuthStorage.STATE_NO_AUTH, null)
        val expected = UserAuth(UserAuth.State.NO_AUTH, null)
        val actual = converter.toDomain(source)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `no auth item from domain EXPECT storage no auth item`() {
        val source = UserAuth(UserAuth.State.NO_AUTH, null)
        val expected = UserAuthStorage(UserAuthStorage.STATE_NO_AUTH, null)
        val actual = converter.toStorage(source)
        Assert.assertEquals(expected, actual)
    }
}