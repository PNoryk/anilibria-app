package ru.radiationx.anilibria.ui.fragments.auth.social

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.fragment_auth_social.*
import kotlinx.android.synthetic.main.fragment_main_base.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.radiationx.anilibria.R
import ru.radiationx.anilibria.presentation.auth.social.AuthSocialPresenter
import ru.radiationx.anilibria.presentation.auth.social.AuthSocialView
import ru.radiationx.anilibria.ui.common.webpage.WebPageStateWebViewClient
import ru.radiationx.anilibria.ui.common.webpage.WebPageViewState
import ru.radiationx.anilibria.ui.common.webpage.compositeWebViewClientOf
import ru.radiationx.anilibria.ui.fragments.BaseFragment
import ru.radiationx.anilibria.ui.fragments.auth.AnalyticsWebViewClient
import ru.radiationx.anilibria.ui.fragments.auth.AuthPatternWebViewClient
import ru.radiationx.shared.ktx.android.gone
import ru.radiationx.shared_app.AppLinkHelper
import ru.radiationx.shared_app.di.injectDependencies
import tv.anilibria.core.types.RelativeUrl
import tv.anilibria.feature.auth.data.domain.SocialAuthService
import tv.anilibria.plugin.data.analytics.LifecycleTimeCounter
import javax.inject.Inject


/**
 * Created by radiationx on 31.12.17.
 */
class AuthSocialFragment : BaseFragment(), AuthSocialView {

    companion object {
        private const val ARG_KEY = "key"

        fun newInstance(key: String) = AuthSocialFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_KEY, key)
            }
        }
    }

    private val authPatternWebViewClient by lazy { AuthPatternWebViewClient(presenter::onSuccessAuthResult) }

    private val analyticsWebViewClient by lazy { AnalyticsWebViewClient(presenter::sendAnalyticsPageError) }

    private val webPageWebViewClient by lazy { WebPageStateWebViewClient(presenter::onPageStateChanged) }

    private val compositeWebViewClient by lazy {
        compositeWebViewClientOf(
            authPatternWebViewClient,
            analyticsWebViewClient,
            webPageWebViewClient
        )
    }

    private val useTimeCounter by lazy {
        LifecycleTimeCounter(presenter::submitUseTime)
    }

    @Inject
    lateinit var appLinkHelper: AppLinkHelper

    @InjectPresenter
    lateinit var presenter: AuthSocialPresenter

    @ProvidePresenter
    fun providePresenter(): AuthSocialPresenter =
        getDependency(AuthSocialPresenter::class.java, screenScope)

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies(screenScope)
        super.onCreate(savedInstanceState)
        arguments?.let {
            presenter.argKey = it.getString(ARG_KEY, presenter.argKey)
        }
    }

    override fun getLayoutResource(): Int = R.layout.fragment_auth_social

    override val statusBarVisible: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycle.addObserver(useTimeCounter)
        appbarLayout.gone()

        webView.apply {
            settings.apply {
                setAppCacheEnabled(false)
                cacheMode = WebSettings.LOAD_NO_CACHE
            }
            webViewClient = compositeWebViewClient
        }

        errorView.setPrimaryButtonClickListener {
            webView.reload()
        }

        cookieView.setPrimaryButtonClickListener {
            presenter.onContinueClick()
        }
        cookieView.setSecondaryClickListener {
            presenter.onClearDataClick()
        }
    }

    override fun onBackPressed(): Boolean {
        return false
    }

    override fun onDestroyView() {
        webView.stopLoading()
        super.onDestroyView()
    }

    override fun loadPage(data: SocialAuthService) {
        authPatternWebViewClient.resultPattern = data.resultPattern.pattern
        webView.loadUrl(data.socialUrl.value)
    }

    override fun showState(state: AuthSocialScreenState) {
        Log.d("kekeke", "show state $state")
        val anyLoading = state.isAuthProgress || state.pageState == WebPageViewState.Loading
        progressBarWv.isVisible = anyLoading
        webView.isVisible =
            state.pageState == WebPageViewState.Success && !anyLoading && !state.showClearCookies
        errorView.isVisible = state.pageState is WebPageViewState.Error
        cookieView.isVisible = state.showClearCookies
    }

    override fun showError() {
        AlertDialog.Builder(requireContext())
            .setMessage("Не найден связанный аккаунт.\n\nЕсли у вас уже есть аккаунт на сайте AniLibria.tv, то привяжите этот аккаунт в личном кабинете.\n\nЕсли аккаунта нет, то зарегистрируйте его на сайте.")
            .setPositiveButton("Перейти") { _, _ ->
                appLinkHelper.openLink(RelativeUrl("/pages/cp.php"))
            }
            .setNegativeButton("Отмена", null)
            .show()
            .setOnDismissListener {
                presenter.onUserUnderstandWhatToDo()
            }
    }
}