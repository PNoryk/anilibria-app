package ru.radiationx.data.api.service.auth

import io.reactivex.Single
import retrofit2.http.FieldMap
import ru.radiationx.data.api.remote.auth.OtpInfoResponse
import ru.radiationx.data.api.remote.auth.SocialServiceResponse
import ru.radiationx.data.api.remote.common.ApiBaseResponse

interface AuthApi {

    fun signIn(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<Any>>
    fun signOut(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<Any>>

    fun getSocialServices(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<List<SocialServiceResponse>>>
    fun signInSocial(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<Any>>

    fun getOtpInfo(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<OtpInfoResponse>>
    fun acceptOtp(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<Any>>
    fun signInOtp(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<Any>>
}