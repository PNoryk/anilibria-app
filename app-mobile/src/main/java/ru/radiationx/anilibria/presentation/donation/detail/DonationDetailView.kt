package ru.radiationx.anilibria.presentation.donation.detail

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType
import tv.anilibria.feature.donation.data.domain.DonationInfo


interface DonationDetailView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showData(data: DonationInfo)

    @StateStrategyType(SkipStrategy::class)
    fun openYooMoney()

    @StateStrategyType(SkipStrategy::class)
    fun openContentDialog(tag: String)
}