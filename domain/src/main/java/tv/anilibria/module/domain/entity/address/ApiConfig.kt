package tv.anilibria.module.data.restapi.entity.app.address

import tv.anilibria.module.domain.entity.address.ApiAddress

data class ApiConfig(
    val addresses: List<ApiAddress>
)