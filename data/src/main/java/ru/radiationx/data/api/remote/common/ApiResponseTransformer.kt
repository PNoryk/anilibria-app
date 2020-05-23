package ru.radiationx.data.api.remote.common

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.SingleTransformer
import retrofit2.HttpException

class ApiResponseTransformer<T> : SingleTransformer<ApiBaseResponse<T>, T> {

    override fun apply(upstream: Single<ApiBaseResponse<T>>): SingleSource<T> = upstream
        .onErrorResumeNext { Single.error(fetchError(it)) }
        .flatMap { it.handleError() }
        .map { it.data!! }

    private fun <T> ApiBaseResponse<T>.handleError(): Single<ApiBaseResponse<T>> = when {
        status -> Single.just(this)
        error != null -> Single.error(error)
        else -> Single.error(RuntimeException("Wrong response"))
    }

    private fun fetchError(exception: Throwable): Throwable = if (exception is HttpException && exception.response().errorBody() != null) {
        val errorBody = exception.response().errorBody()!!
        try {
            val json = errorBody.string()
            val apiResponse = Gson().fromJson(json, ApiBaseResponse::class.java)
            apiResponse.error!!
        } catch (ex: JsonSyntaxException) {
            ex.printStackTrace()
            exception
        }
    } else {
        exception
    }
}