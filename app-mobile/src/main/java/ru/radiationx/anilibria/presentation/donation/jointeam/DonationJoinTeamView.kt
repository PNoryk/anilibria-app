package ru.radiationx.anilibria.presentation.donation.jointeam

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import tv.anilibria.module.domain.entity.donation.DonationDialog

interface DonationJoinTeamView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showData(data: DonationDialog)
}