package tv.anilibria.module.domain.remote

import io.reactivex.Single
import tv.anilibria.module.domain.entity.page.PageLibria
import tv.anilibria.module.domain.entity.page.VkComments

interface PageRemoteDataSource {
    fun getPage(pagePath: String): Single<PageLibria>
    fun getComments(): Single<VkComments>
}