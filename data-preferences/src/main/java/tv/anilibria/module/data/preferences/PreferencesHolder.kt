package tv.anilibria.module.data.preferences

import tv.anilibria.plugin.data.storage.ObservableData

/**
 * Created by radiationx on 03.02.18.
 */
interface PreferencesHolder {
    companion object {
        const val QUALITY_NO = -1
        const val QUALITY_SD = 0
        const val QUALITY_HD = 1
        const val QUALITY_ALWAYS = 2
        const val QUALITY_FULL_HD = 3

        const val PLAYER_TYPE_NO = -1
        const val PLAYER_TYPE_EXTERNAL = 0
        const val PLAYER_TYPE_INTERNAL = 1
        const val PLAYER_TYPE_ALWAYS = 2

        const val PIP_BUTTON = 0
        const val PIP_AUTO = 1
    }

    val newDonationRemind: ObservableData<Boolean>

    val releaseRemind: ObservableData<Boolean>

    val searchRemind: ObservableData<Boolean>

    val episodesIsReverse: ObservableData<Boolean>

    val quality: ObservableData<PlayerQuality>

    val playerType: ObservableData<PlayerType>

    val playSpeed: ObservableData<Float>

    val pipControl: ObservableData<PlayerPipMode>

    val notificationsAll: ObservableData<Boolean>

    val notificationsService: ObservableData<Boolean>

}