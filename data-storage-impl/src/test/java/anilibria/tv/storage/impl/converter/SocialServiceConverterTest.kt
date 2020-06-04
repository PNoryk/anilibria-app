package anilibria.tv.storage.impl.converter

import anilibria.tv.domain.entity.auth.SocialService
import anilibria.tv.domain.entity.menu.LinkMenu
import anilibria.tv.storage.impl.entity.LinkMenuStorage
import anilibria.tv.storage.impl.entity.SocialServiceStorage
import org.junit.Assert
import org.junit.Test

class SocialServiceConverterTest {

    private val converter = SocialServiceConverter()

    private val listDomain: List<SocialService> = listOf(
        SocialService(
            key = "soc1",
            title = "amazing service 1",
            socialUrl = "amazing://service.1",
            resultPattern = "its amazing",
            errorUrlPattern = "not amazing"
        ),
        SocialService(
            key = "soc2",
            title = "regular service 2",
            socialUrl = "regular://service.2",
            resultPattern = "its regular",
            errorUrlPattern = "not regular"
        )
    )

    private val listStorage: List<SocialServiceStorage> = listOf(
        SocialServiceStorage(
            key = "soc1",
            title = "amazing service 1",
            socialUrl = "amazing://service.1",
            resultPattern = "its amazing",
            errorUrlPattern = "not amazing"
        ),
        SocialServiceStorage(
            key = "soc2",
            title = "regular service 2",
            socialUrl = "regular://service.2",
            resultPattern = "its regular",
            errorUrlPattern = "not regular"
        )
    )

    @Test
    fun `item from storage EXPECT domain item`() {
        val source = listStorage.first()
        val expected = listDomain.first()
        val actual = converter.toDomain(source)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `item from domain EXPECT storage item`() {
        val source = listDomain.first()
        val expected = listStorage.first()
        val actual = converter.toStorage(source)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `items from storage EXPECT domain items`() {
        val source = listStorage
        val expected = listDomain
        val actual = converter.toDomain(source)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `items from domain EXPECT storage items`() {
        val source = listDomain
        val expected = listStorage
        val actual = converter.toStorage(source)
        Assert.assertEquals(expected, actual)
    }
}