package anilibria.tv.api.impl.converter

import anilibria.tv.api.impl.entity.checker.UpdateLinkResponse
import anilibria.tv.api.impl.entity.checker.UpdateResponse
import anilibria.tv.domain.entity.checker.Update
import anilibria.tv.domain.entity.checker.UpdateLink
import org.junit.Assert.assertEquals
import org.junit.Test

class CheckerConverterTest {

    private val converter = CheckerConverter()

    private val response = UpdateResponse(
        versionCode = "code",
        versionName = "name",
        versionBuild = "build",
        buildDate = "date",
        links = listOf(
            UpdateLinkResponse("name1", "url1", "site"),
            UpdateLinkResponse("name2", "url2", "file")
        ),
        important = listOf("important1", "important2"),
        added = listOf("added1"),
        fixed = listOf(),
        changed = listOf("changed")
    )

    private val domain = Update(
        versionCode = "code",
        versionName = "name",
        versionBuild = "build",
        buildDate = "date",
        links = listOf(
            UpdateLink("name1", "url1", "site"),
            UpdateLink("name2", "url2", "file")
        ),
        important = listOf("important1", "important2"),
        added = listOf("added1"),
        fixed = listOf(),
        changed = listOf("changed")
    )

    @Test
    fun `from response EXPECT domain`() {
        val actual = converter.toDomain(response)
        assertEquals(domain, actual)
    }
}