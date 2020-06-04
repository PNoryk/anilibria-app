package anilibria.tv.storage.impl.converter

import anilibria.tv.domain.entity.auth.SocialService
import anilibria.tv.domain.entity.user.User
import anilibria.tv.storage.impl.entity.SocialServiceStorage
import anilibria.tv.storage.impl.entity.UserStorage
import org.junit.Assert
import org.junit.Test

class UserConverterTest {

    private val converter = UserConverter()

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
        val actual = converter.toDomain(userStorage)
        Assert.assertEquals(userDomain, actual)
    }

    @Test
    fun `item from domain EXPECT storage item`() {
        val actual = converter.toStorage(userDomain)
        Assert.assertEquals(userStorage, actual)
    }
}