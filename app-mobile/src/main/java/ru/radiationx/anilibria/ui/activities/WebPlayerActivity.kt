package ru.radiationx.anilibria.ui.activities

import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.webkit.*
import kotlinx.android.synthetic.main.activity_moon.*
import ru.radiationx.anilibria.App
import ru.radiationx.anilibria.R
import ru.radiationx.anilibria.extension.generateWithTheme
import ru.radiationx.shared.ktx.android.toException
import ru.radiationx.shared_app.AppLinkHelper
import ru.radiationx.shared_app.di.injectDependencies
import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.core.types.RelativeUrl
import tv.anilibria.feature.networkconfig.data.address.ApiConfigController
import tv.anilibria.feature.content.data.BaseUrlHelper
import tv.anilibria.feature.analytics.api.features.WebPlayerAnalytics
import tv.anilibria.app.mobile.preferences.AppTheme
import tv.anilibria.plugin.data.analytics.LifecycleTimeCounter
import java.util.regex.Pattern
import javax.inject.Inject


class WebPlayerActivity : BaseActivity() {

    companion object {
        const val ARG_URL = "iframe_url"
        const val ARG_RELEASE_LINK = "release_link"
    }

    private val argUrl: AbsoluteUrl by lazy {
        requireNotNull(intent?.getParcelableExtra(ARG_URL))
    }
    private val argReleaseLink: RelativeUrl by lazy {
        requireNotNull(intent?.getParcelableExtra(ARG_RELEASE_LINK))
    }

    private val useTimeCounter by lazy {
        LifecycleTimeCounter(webPlayerAnalytics::useTime)
    }

    @Inject
    lateinit var apiConfig: ApiConfigController

    @Inject
    lateinit var webPlayerAnalytics: WebPlayerAnalytics

    @Inject
    lateinit var urlHelper: BaseUrlHelper

    @Inject
    lateinit var appLinkHelper: AppLinkHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(useTimeCounter)


        if (argUrl.value.isEmpty()) {
            finish()
            return
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_moon)
        supportActionBar?.hide()

        webView.settings.apply {
            setAppCacheEnabled(false)
            cacheMode = WebSettings.LOAD_NO_CACHE
            javaScriptEnabled = true
        }
        webView.webViewClient = object : WebViewClient() {
            @Suppress("OverridingDeprecatedMember")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                val matcher =
                    Pattern.compile("https?:\\/\\/(?:vk\\.com\\/video_ext|streamguard\\.cc|kodik\\.info)")
                        .matcher(url)
                return if (matcher.find()) {
                    false
                } else {
                    appLinkHelper.openLink(AbsoluteUrl(url.orEmpty()))
                    true
                }
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                webPlayerAnalytics.loaded()
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                super.onReceivedSslError(view, handler, error)
                webPlayerAnalytics.error(error.toException())
            }

            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
                super.onReceivedHttpError(view, request, errorResponse)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && view?.url == request?.url?.toString()) {
                    webPlayerAnalytics.error(errorResponse.toException(request))
                }
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && view?.url == request?.url?.toString()) {
                    webPlayerAnalytics.error(error.toException(request))
                }
            }
        }

        loadUrl()
    }

    private fun loadUrl() {
        val releaseUrl = urlHelper.makeWidget(argReleaseLink)

        val template = App.instance.videoPageTemplate
        template.setVariableOpt("iframe_url", argUrl.value)

        webView.easyLoadData(releaseUrl?.value.orEmpty(), template.generateWithTheme(AppTheme.DARK))
    }
}
