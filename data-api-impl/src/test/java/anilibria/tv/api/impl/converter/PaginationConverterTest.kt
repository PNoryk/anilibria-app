package anilibria.tv.api.impl.converter

import anilibria.tv.api.impl.entity.checker.UpdateLinkResponse
import anilibria.tv.api.impl.entity.checker.UpdateResponse
import anilibria.tv.api.impl.entity.comments.CommentsInfoResponse
import anilibria.tv.api.impl.entity.pagination.PaginatedResponse
import anilibria.tv.api.impl.entity.pagination.PaginationResponse
import anilibria.tv.domain.entity.checker.Update
import anilibria.tv.domain.entity.checker.UpdateLink
import anilibria.tv.domain.entity.comments.CommentsInfo
import anilibria.tv.domain.entity.pagination.Paginated
import anilibria.tv.domain.entity.pagination.Pagination
import org.junit.Assert.assertEquals
import org.junit.Test

class PaginationConverterTest {

    private val converter = PaginationConverter()

    private val response = PaginatedResponse(
        items = listOf("item1", "item2"),
        pagination = PaginationResponse(1, 2, 2, 4)
    )

    private val domain = Paginated(
        items = listOf("domain_item1", "domain_item2"),
        pagination = Pagination(1, 2, 2, 4)
    )

    private val mapper: (String) -> String = { "domain_$it" }

    @Test
    fun `from response EXPECT domain`() {
        val actual = converter.toDomain(response, mapper)
        assertEquals(domain, actual)
    }
}