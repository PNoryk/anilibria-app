package ru.radiationx.data.entity.app.auth

import ru.radiationx.data.datasource.remote.ApiError

@Deprecated("old data")
open class ApiWrapperException(apiError: ApiError) :
    RuntimeException(apiError.message, apiError)

@Deprecated("old data")
data class AlreadyAuthorizedException(val apiError: ApiError) : ApiWrapperException(apiError)

@Deprecated("old data")
data class EmptyFieldException(val apiError: ApiError) : ApiWrapperException(apiError)

@Deprecated("old data")
data class WrongUserAgentException(val apiError: ApiError) : ApiWrapperException(apiError)

@Deprecated("old data")
data class InvalidUserException(val apiError: ApiError) : ApiWrapperException(apiError)

@Deprecated("old data")
data class Wrong2FaCodeException(val apiError: ApiError) : ApiWrapperException(apiError)

@Deprecated("old data")
data class WrongPasswordException(val apiError: ApiError) : ApiWrapperException(apiError)
