package tv.anilibria.module.data

import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.module.data.repos.OtherRepository

class VkCommentsClientImpl(
    private val otherRepository: OtherRepository
) : VkCommentsClient {

    override suspend fun getBody(url: AbsoluteUrl): String {
        return otherRepository.getDirectBody(url)
    }
}