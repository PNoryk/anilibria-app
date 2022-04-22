package tv.anilibria.feature.networkconfig.data

import toothpick.InjectConstructor
import tv.anilibria.plugin.data.storage.InMemoryDataHolder
import tv.anilibria.plugin.data.storage.ObservableData

@InjectConstructor
class ConfigPingCache {

    val proxies = ObservableData<Map<String, Long>>(InMemoryDataHolder(emptyMap()))
}