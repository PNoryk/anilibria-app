package tv.anilibria.module.data.network

import okhttp3.Interceptor
import okhttp3.Response

class AppInfoInterceptor(
    private val sharedBuildConfig: SharedBuildConfig
) : Interceptor {

    companion object{
        private const val USER_AGENT =
            "mobileApp Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.170 Safari/537.36 OPR/53.0.2907.68"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val additionalHeadersRequest = chain.request()
            .newBuilder()
            .header("mobileApp", "true")
            .header("App-Id", sharedBuildConfig.applicationId)
            .header("App-Ver-Name", sharedBuildConfig.versionName)
            .header("App-Ver-Code", sharedBuildConfig.versionCode.toString())
            .header("User-Agent", USER_AGENT)
            .build()
        return chain.proceed(additionalHeadersRequest)
    }
}