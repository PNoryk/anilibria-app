package ru.radiationx.data.api.service.auth

import io.reactivex.Single
import ru.radiationx.data.api.remote.auth.OtpInfoResponse
import ru.radiationx.data.api.remote.auth.SocialServiceResponse
import ru.radiationx.data.api.remote.common.ApiBaseResponse

interface AuthApi {

    fun signIn(): Single<ApiBaseResponse<Any>>
    fun signOut(): Single<ApiBaseResponse<Any>>

    fun getSocialServices(): Single<ApiBaseResponse<List<SocialServiceResponse>>>
    fun signInSocial(): Single<ApiBaseResponse<Any>>

    fun getOtpInfo(): Single<ApiBaseResponse<OtpInfoResponse>>
    fun acceptOtp(): Single<ApiBaseResponse<Any>>
    fun signInOtp(): Single<ApiBaseResponse<Any>>
}