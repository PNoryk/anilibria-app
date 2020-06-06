package anilibria.tv.api.impl.common

import anilibria.tv.api.impl.entity.common.ApiBaseResponse
import anilibria.tv.api.impl.entity.common.ApiErrorResponse
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.lang.RuntimeException

class ApiResponseTransformerTest {

    private val data = listOf("privet", "kak dela")
    private val error = ApiErrorResponse(404, "no", "may be yesn't")

    private val httpApiException = HttpException(
        Response.error<ApiBaseResponse<List<String>>>(
            500, ResponseBody.create(
                MediaType.get("application/json; charset=UTF-8"),
                """{"error":{"code":500,"message":"sorry"}}"""
            )
        )
    )
    private val httpApiError = ApiErrorResponse(500, "sorry", null)

    private val otherException = RuntimeException("other")

    @Test
    fun `data response EXPECT success`() {
        val response = ApiBaseResponse(true, data, null)
        Single
            .just(response)
            .handleApiResponse()
            .test()
            .assertValue(data)
    }

    @Test
    fun `error response EXPECT error`() {
        val response = ApiBaseResponse<List<String>>(false, null, error)
        Single
            .just(response)
            .handleApiResponse()
            .test()
            .assertError(error)
    }

    @Test
    fun `http error response EXPECT error`() {
        Single
            .error<ApiBaseResponse<List<String>>>(httpApiException)
            .handleApiResponse()
            .test()
            .assertError(httpApiError)
    }

    @Test
    fun `other error response EXPECT error`() {
        Single
            .error<ApiBaseResponse<List<String>>>(otherException)
            .handleApiResponse()
            .test()
            .assertError(otherException)
    }
}