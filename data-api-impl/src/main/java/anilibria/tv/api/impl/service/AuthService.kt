package anilibria.tv.api.impl.service

import io.reactivex.Single
import retrofit2.http.FieldMap
import anilibria.tv.api.impl.entity.auth.OtpInfoResponse
import anilibria.tv.api.impl.entity.auth.SocialServiceResponse
import anilibria.tv.api.impl.entity.common.ApiBaseResponse

interface AuthService {

    fun signIn(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<Any>>
    fun signOut(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<Any>>

    fun getSocialServices(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<List<SocialServiceResponse>>>
    fun signInSocial(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<Any>>

    fun getOtpInfo(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<OtpInfoResponse>>
    fun acceptOtp(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<Any>>
    fun signInOtp(@FieldMap fields: Map<String, String>): Single<ApiBaseResponse<Any>>
}