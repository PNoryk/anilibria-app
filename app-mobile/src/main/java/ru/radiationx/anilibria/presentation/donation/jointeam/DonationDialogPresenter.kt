package ru.radiationx.anilibria.presentation.donation.jointeam

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.radiationx.shared_app.AppLinkHelper
import ru.radiationx.anilibria.presentation.common.BasePresenter
import ru.radiationx.anilibria.ui.common.ErrorHandler
import ru.terrakok.cicerone.Router
import toothpick.InjectConstructor
import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.module.data.analytics.features.DonationDialogAnalytics
import tv.anilibria.module.data.repos.DonationRepository
import tv.anilibria.module.domain.entity.donation.DonationContentButton
import tv.anilibria.module.domain.entity.donation.DonationDialog

@InjectConstructor
class DonationDialogPresenter(
    router: Router,
    private val donationRepository: DonationRepository,
    private val errorHandler: ErrorHandler,
    private val analytics: DonationDialogAnalytics,
    private val appLinkHelper: AppLinkHelper
) : BasePresenter<DonationJoinTeamView>(router) {

    private var currentData: DonationDialog? = null

    var argTag: String? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        donationRepository
            .observerDonationInfo()
            .onEach {
                val donationDialog = it.contentDialogs.find { it.tag == argTag }
                if (donationDialog != null) {
                    currentData = donationDialog
                    viewState.showData(donationDialog)
                }
            }
            .catch {
                errorHandler.handle(it)
            }
            .launchIn(viewModelScope)
    }

    fun onLinkClick(url: String) {
        analytics.linkClick(argTag.toString(), url)
        appLinkHelper.openLink(AbsoluteUrl(url))
    }

    fun onButtonClick(button: DonationContentButton) {
        analytics.buttonClick(argTag.toString(), button.text)
        appLinkHelper.openLink(button.link)
    }
}