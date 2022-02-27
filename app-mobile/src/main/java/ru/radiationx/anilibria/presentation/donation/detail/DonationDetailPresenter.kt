package ru.radiationx.anilibria.presentation.donation.detail

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.radiationx.shared_app.AppLinkHelper
import ru.radiationx.anilibria.presentation.common.BasePresenter
import ru.terrakok.cicerone.Router
import toothpick.InjectConstructor
import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.module.data.analytics.AnalyticsConstants
import tv.anilibria.module.data.analytics.features.DonationDetailAnalytics
import tv.anilibria.module.data.analytics.features.DonationDialogAnalytics
import tv.anilibria.module.data.analytics.features.DonationYooMoneyAnalytics
import tv.anilibria.feature.donation.data.DonationRepository
import tv.anilibria.module.domain.entity.donation.DonationContentButton
import tv.anilibria.module.domain.entity.donation.DonationInfo

@InjectConstructor
class DonationDetailPresenter(
    router: Router,
    private val donationRepository: DonationRepository,
    private val detailAnalytics: DonationDetailAnalytics,
    private val yooMoneyAnalytics: DonationYooMoneyAnalytics,
    private val dialogAnalytics: DonationDialogAnalytics,
    private val appLinkHelper: AppLinkHelper
) : BasePresenter<DonationDetailView>(router) {

    private var currentData: DonationInfo? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewModelScope.launch {
            runCatching {
                donationRepository.requestUpdate()
            }.onFailure {
                it.printStackTrace()
            }
        }

        donationRepository
            .observerDonationInfo()
            .onEach {
                currentData = it
                viewState.showData(it)
            }
            .catch {
                it.printStackTrace()
            }
            .launchIn(viewModelScope)
    }

    fun onLinkClick(url: String) {
        detailAnalytics.linkClick(url)
        appLinkHelper.openLink(AbsoluteUrl(url))
    }

    fun onButtonClick(button: DonationContentButton) {
        detailAnalytics.buttonClick(button.text)
        val info = currentData ?: return
        val buttonTag = button.tag
        val buttonLink = button.link

        val dialog = buttonTag?.let { tag -> info.contentDialogs.find { it.tag == tag } }
        val yoomoneyDialog = buttonTag
            ?.takeIf { it == DonationInfo.YOOMONEY_TAG }
            ?.let { info.yooMoneyDialog }

        when {
            yoomoneyDialog != null -> {
                yooMoneyAnalytics.open(AnalyticsConstants.screen_donation_detail)
                viewState.openYooMoney()
            }
            dialog != null -> {
                dialogAnalytics.open(AnalyticsConstants.screen_donation_detail, dialog.tag)
                viewState.openContentDialog(dialog.tag)
            }
            buttonLink != null -> {
                appLinkHelper.openLink(buttonLink)
            }
        }
    }

}