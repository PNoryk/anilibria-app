package anilibria.tv.api.impl.converter

import org.junit.Assert.assertEquals
import org.junit.Test

class DayConverterTest {

    private val converter = DayConverter()

    private val response = listOf("1", "2", "3", "4", "5", "6", "7")
    private val domain = listOf(2, 3, 4, 5, 6, 7, 1)

    @Test
    fun `from response EXPECT domain`() {
        val actual = response.map { converter.toDomain(it) }
        assertEquals(domain, actual)
    }

    @Test
    fun `from domain EXPECT response`() {
        val actual = domain.map { converter.toResponse(it) }
        assertEquals(response, actual)
    }
}