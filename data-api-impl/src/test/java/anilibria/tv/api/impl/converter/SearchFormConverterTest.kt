package anilibria.tv.api.impl.converter

import anilibria.tv.api.impl.entity.search.SearchFormRequest
import anilibria.tv.domain.entity.search.SearchForm
import junit.framework.Assert.assertEquals
import org.junit.Test

class SearchFormConverterTest {

    private val converter = SearchFormConverter()

    @Test
    fun `empty form EXPECT success`() {
        val domain = SearchForm()
        val request = SearchFormRequest("", "", "", "2", "1")

        val actual = converter.toRequest(domain)
        assertEquals(request, actual)
    }

    @Test
    fun `full form EXPECT success`() {
        val domain = SearchForm(listOf("year1", "year2"), listOf("season1"), listOf("genre1", "genre2", "genre3"))
        val request = SearchFormRequest("year1,year2", "season1", "genre1,genre2,genre3", "2", "1")

        val actual = converter.toRequest(domain)
        assertEquals(request, actual)
    }

    @Test
    fun `check date EXPECT success`() {
        val domainRating = SearchForm(sort = SearchForm.Sort.RATING)
        val domainDate = SearchForm(sort = SearchForm.Sort.DATE)
        val requestRating = SearchFormRequest("", "", "", "2", "1")
        val requestDate = SearchFormRequest("", "", "", "1", "1")

        val actualRating = converter.toRequest(domainRating)
        val actualDate = converter.toRequest(domainDate)
        assertEquals(requestRating, actualRating)
        assertEquals(requestDate, actualDate)
    }

    @Test
    fun `check complete EXPECT success`() {
        val domainFalse = SearchForm(onlyCompleted = false)
        val domainTrue = SearchForm(onlyCompleted = true)
        val requestFalse = SearchFormRequest("", "", "", "2", "1")
        val requestTrue = SearchFormRequest("", "", "", "2", "2")

        val actualFalse = converter.toRequest(domainFalse)
        val actualTrue = converter.toRequest(domainTrue)
        assertEquals(requestFalse, actualFalse)
        assertEquals(requestTrue, actualTrue)
    }
}