package tv.anilibria.feature.networkconfig.data

import tv.anilibria.plugin.data.storage.ObservableData

class ConfigPingCache {

    val proxies = ObservableData<Map<String, Long>>()
}