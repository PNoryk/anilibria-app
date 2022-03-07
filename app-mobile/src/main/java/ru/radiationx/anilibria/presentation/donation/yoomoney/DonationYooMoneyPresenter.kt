package ru.radiationx.anilibria.presentation.donation.yoomoney

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.radiationx.anilibria.presentation.common.BasePresenter
import ru.radiationx.anilibria.presentation.donation.infra.DonationYooMoneyState
import ru.radiationx.anilibria.ui.common.ErrorHandler
import ru.radiationx.shared_app.AppLinkHelper
import ru.terrakok.cicerone.Router
import toothpick.InjectConstructor
import tv.anilibria.feature.donation.data.DonationRepository
import tv.anilibria.feature.donation.data.domain.yoomoney.YooMoneyDialog
import tv.anilibria.feature.data.analytics.features.DonationYooMoneyAnalytics
import tv.anilibria.feature.data.analytics.features.model.AnalyticsDonationAmountType
import tv.anilibria.feature.data.analytics.features.model.AnalyticsDonationPaymentType

@InjectConstructor
class DonationYooMoneyPresenter(
    router: Router,
    private val donationRepository: DonationRepository,
    private val errorHandler: ErrorHandler,
    private val analytics: DonationYooMoneyAnalytics,
    private val appLinkHelper: AppLinkHelper
) : BasePresenter<DonationYooMoneyView>(router) {

    private var currentState = DonationYooMoneyState()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        donationRepository
            .observerDonationInfo()
            .onEach {
                val yooMoneyInfo = it.yooMoneyDialog
                val newState = currentState.copy(
                    data = yooMoneyInfo,
                    amountType = DonationYooMoneyState.AmountType.PRESET,
                    selectedAmount = yooMoneyInfo?.amounts?.defaultValue,
                    selectedPaymentTypeId = yooMoneyInfo?.paymentTypes?.selectedId
                )
                tryUpdateState(newState)
            }
            .catch {
                errorHandler.handle(it)
            }
            .launchIn(viewModelScope)
    }

    fun setSelectedAmount(value: Int?) {
        val newState = currentState.copy(
            selectedAmount = value,
            amountType = DonationYooMoneyState.AmountType.PRESET
        )
        tryUpdateState(newState)
    }

    fun setCustomAmount(value: Int?) {
        val newState = currentState.copy(
            customAmount = value,
            amountType = DonationYooMoneyState.AmountType.CUSTOM
        )
        tryUpdateState(newState)
    }

    fun setPaymentType(typeId: String?) {
        val newState = currentState.copy(selectedPaymentTypeId = typeId)
        tryUpdateState(newState)
    }

    fun submitHelpClickAnalytics() {
        analytics.helpClick()
    }

    fun onAcceptClick() {
        val amount = currentState.getAmount()
        val paymentTypeId = currentState.selectedPaymentTypeId ?: return
        val form = currentState.data?.form ?: return
        analytics.acceptClick(
            amount,
            currentState.amountType.toAnalytics(),
            paymentTypeId.toAnalyticsPaymentType()
        )
        viewState.setRefreshing(true)
        viewModelScope.launch {
            runCatching {
                donationRepository.createYooMoneyPayLink(amount, paymentTypeId, form)
            }.onSuccess {
                viewState.setRefreshing(false)
                appLinkHelper.openLink(it)
                viewState.close()
            }.onFailure {
                viewState.setRefreshing(false)
                errorHandler.handle(it)
            }
        }
    }

    private fun DonationYooMoneyState.withValidation(): DonationYooMoneyState {
        val isValidAmount = getAmount() > 0
        val containsType = data?.paymentTypes?.items?.any { it.id == selectedPaymentTypeId } == true
        val isValidType = selectedPaymentTypeId != null && containsType
        return copy(acceptEnabled = isValidAmount && isValidType)
    }

    private fun DonationYooMoneyState.getAmount(): Int = when (amountType) {
        DonationYooMoneyState.AmountType.PRESET -> selectedAmount ?: 0
        DonationYooMoneyState.AmountType.CUSTOM -> customAmount ?: 0
    }

    private fun tryUpdateState(newState: DonationYooMoneyState) {
        val withValidationState = newState.withValidation()
        if (currentState != withValidationState) {
            currentState = withValidationState
            viewState.showData(currentState)
        }
    }

    private fun DonationYooMoneyState.AmountType.toAnalytics() = when (this) {
        DonationYooMoneyState.AmountType.PRESET -> AnalyticsDonationAmountType.PRESET
        DonationYooMoneyState.AmountType.CUSTOM -> AnalyticsDonationAmountType.CUSTOM
    }

    private fun String.toAnalyticsPaymentType() = when (this) {
        YooMoneyDialog.TYPE_ID_ACCOUNT -> AnalyticsDonationPaymentType.ACCOUNT
        YooMoneyDialog.TYPE_ID_CARD -> AnalyticsDonationPaymentType.CARD
        YooMoneyDialog.TYPE_ID_MOBILE -> AnalyticsDonationPaymentType.MOBILE
        else -> null
    }
}