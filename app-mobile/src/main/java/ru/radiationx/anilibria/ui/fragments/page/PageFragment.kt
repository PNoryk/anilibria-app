package ru.radiationx.anilibria.ui.fragments.page

import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.*
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.fragment_main_base.*
import kotlinx.android.synthetic.main.fragment_webview.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.radiationx.anilibria.App
import ru.radiationx.anilibria.R
import ru.radiationx.anilibria.extension.generateWithTheme
import ru.radiationx.anilibria.extension.getWebStyleType
import ru.radiationx.anilibria.presentation.page.PagePresenter
import ru.radiationx.anilibria.presentation.page.PageView
import ru.radiationx.anilibria.ui.fragments.BaseFragment
import ru.radiationx.anilibria.ui.widgets.ExtendedWebView
import ru.radiationx.anilibria.utils.ToolbarHelper
import ru.radiationx.shared.ktx.android.putExtra
import ru.radiationx.shared.ktx.android.toBase64
import ru.radiationx.shared.ktx.android.toException
import ru.radiationx.shared.ktx.android.visible
import ru.radiationx.shared_app.di.injectDependencies
import tv.anilibria.core.types.RelativeUrl
import tv.anilibria.feature.page.data.domain.PageLibria
import tv.anilibria.feature.analytics.api.features.PageAnalytics
import tv.anilibria.app.mobile.preferences.PreferencesStorage
import tv.anilibria.plugin.data.analytics.LifecycleTimeCounter
import tv.anilibria.plugin.data.network.BaseUrlsProvider
import javax.inject.Inject

/**
 * Created by radiationx on 13.01.18.
 */
class PageFragment : BaseFragment(), PageView, ExtendedWebView.JsLifeCycleListener {

    companion object {
        private const val ARG_PATH: String = "page_path"
        private const val ARG_TITLE: String = "page_title"
        private const val WEB_VIEW_SCROLL_Y = "wvsy"

        fun newInstance(pagePath: RelativeUrl, title: String? = null) = PageFragment().putExtra {
            putParcelable(ARG_PATH, pagePath)
            putString(ARG_TITLE, title)
        }
    }

    private val useTimeCounter by lazy {
        LifecycleTimeCounter(pageAnalytics::useTime)
    }

    private val pageTitle: String? by lazy { arguments?.getString(ARG_TITLE) }

    @Inject
    lateinit var preferencesStorage: PreferencesStorage

    @Inject
    lateinit var baseUrlsProvider: BaseUrlsProvider

    @Inject
    lateinit var pageAnalytics: PageAnalytics

    private var webViewScrollPos = 0

    @InjectPresenter
    lateinit var presenter: PagePresenter

    @ProvidePresenter
    fun providePagePresenter(): PagePresenter =
        getDependency(PagePresenter::class.java, screenScope)

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies(screenScope)
        super.onCreate(savedInstanceState)
        presenter.pagePath = requireNotNull(arguments?.getParcelable(ARG_PATH))
    }

    override fun getLayoutResource(): Int = R.layout.fragment_webview

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycle.addObserver(useTimeCounter)
        //ToolbarHelper.setTransparent(toolbar, appbarLayout)
        //ToolbarHelper.setScrollFlag(toolbarLayout, AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED)
        ToolbarHelper.fixInsets(toolbar)
        ToolbarHelper.marqueeTitle(toolbar)

        toolbar.apply {
            title = pageTitle ?: "Статическая страница"
            setNavigationOnClickListener { presenter.onBackPressed() }
            setNavigationIcon(R.drawable.ic_toolbar_arrow_back)
        }

        webView.setJsLifeCycleListener(this)

        webView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                pageAnalytics.loaded()
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                super.onReceivedSslError(view, handler, error)
                pageAnalytics.error(error.toException())
            }

            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
                super.onReceivedHttpError(view, request, errorResponse)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && view?.url == request?.url?.toString()) {
                    pageAnalytics.error(errorResponse.toException(request))
                }
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && view?.url == request?.url?.toString()) {
                    pageAnalytics.error(error.toException(request))
                }
            }
        }

        savedInstanceState?.let {
            webViewScrollPos = it.getInt(WEB_VIEW_SCROLL_Y, 0)
        }

        val template = App.instance.staticPageTemplate
        webView.easyLoadData(
            baseUrlsProvider.site.value,
            template.generateWithTheme(preferencesStorage.appTheme.blockingGet())
        )

        preferencesStorage.appTheme.observe().onEach {
            webView?.evalJs("changeStyleType(\"${it.getWebStyleType()}\")")
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onResume() {
        super.onResume()
        webView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        webView?.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webView?.let {
            outState.putInt(WEB_VIEW_SCROLL_Y, it.scrollY)
        }
    }

    override fun onDomContentComplete(actions: ArrayList<String>) {

    }

    override fun onPageComplete(actions: ArrayList<String>) {
        webView?.syncWithJs {
            webView?.scrollTo(0, webViewScrollPos)
        }
    }

    override fun onBackPressed(): Boolean {
        presenter.onBackPressed()
        return true
    }

    override fun setRefreshing(refreshing: Boolean) {
        progressBarWv.visible(refreshing)
    }

    override fun showPage(page: PageLibria) {
        //toolbar.title = page.title
        webView?.evalJs("ViewModel.setText('content','${page.content.text.toBase64()}');")
    }

}