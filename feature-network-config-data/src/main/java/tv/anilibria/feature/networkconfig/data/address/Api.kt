package tv.anilibria.feature.networkconfig.data.address

import tv.anilibria.feature.networkconfig.data.domain.ApiAddress

/* Created by radiationx on 31.10.17. */

object Api {
    val DEFAULT_ADDRESS = ApiAddress(
        tag = "default",
        name = "Стандартный домен",
        desc = "",
        widgetsSite = "https://www.anilibria.tv",
        site = "https://www.anilibria.tv",
        baseImages = "https://static.anilibria.tv/",
        base = "https://www.anilibria.tv",
        api = "https://www.anilibria.tv/public/api/index.php",
        ips = emptyList(),
        proxies = emptyList()
    )
}
