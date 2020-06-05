package anilibria.tv.api.impl.converter

import anilibria.tv.api.impl.entity.checker.UpdateLinkResponse
import anilibria.tv.api.impl.entity.checker.UpdateResponse
import anilibria.tv.api.impl.entity.comments.CommentsInfoResponse
import anilibria.tv.api.impl.entity.user.UserResponse
import anilibria.tv.domain.entity.checker.Update
import anilibria.tv.domain.entity.checker.UpdateLink
import anilibria.tv.domain.entity.comments.CommentsInfo
import anilibria.tv.domain.entity.user.User
import org.junit.Assert.assertEquals
import org.junit.Test

class UserConverterTest {

    private val converter = UserConverter()

    private val response = listOf(
        UserResponse(
            id = 10,
            login = "login",
            avatar = "url"
        ),
        UserResponse(
            id = 10,
            login = "login",
            avatar = null
        )
    )

    private val domain = listOf(
        User(
            id = 10,
            login = "login",
            avatar = "url"
        ),
        User(
            id = 10,
            login = "login",
            avatar = null
        )
    )

    @Test
    fun `from response EXPECT domain`() {
        val actual = response.map { converter.toDomain(it) }
        assertEquals(domain, actual)
    }
}