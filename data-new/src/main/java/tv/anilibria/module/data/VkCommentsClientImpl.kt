package tv.anilibria.module.data

import tv.anilibria.module.data.repos.OtherRepository

class VkCommentsClientImpl(
    private val otherRepository: OtherRepository
) : VkCommentsClient {

    override suspend fun getBody(url: String): String {
        return otherRepository.getDirectBody(url)
    }
}