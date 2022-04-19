package tv.anilibria.feature.donation.data.remote

import toothpick.InjectConstructor
import tv.anilibria.plugin.data.network.ApiWrapper
import tv.anilibria.plugin.data.network.ApiWrapperDeps

@InjectConstructor
class DonationApiWrapper(
    apiWrapperDeps: ApiWrapperDeps
) : ApiWrapper<DonationApi>(DonationApi::class.java, apiWrapperDeps)