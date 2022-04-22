package tv.anilibria.feature.donation.data.di

import toothpick.config.Module
import tv.anilibria.feature.donation.data.DonationRepository
import tv.anilibria.feature.donation.data.local.DonationInfoLocalDataSource
import tv.anilibria.feature.donation.data.remote.DonationApiWrapper
import tv.anilibria.feature.donation.data.remote.DonationRemoteDataSource

class DonationDataFeatureModule : Module() {

    init {
        bind(DonationApiWrapper::class.java).singleton()
        bind(DonationRemoteDataSource::class.java).singleton()
        bind(DonationInfoLocalDataSource::class.java).singleton()
        bind(DonationRepository::class.java).singleton()
    }
}