package tv.anilibria.module.data.network.datasource.holders

import io.reactivex.Observable
import tv.anilibria.module.data.network.entity.app.other.ProfileItem

/**
 * Created by radiationx on 11.01.18.
 */
interface UserHolder {
    fun getUser(): ProfileItem

    fun observeUser(): Observable<ProfileItem>

    fun saveUser(user: ProfileItem)

    fun delete()
}