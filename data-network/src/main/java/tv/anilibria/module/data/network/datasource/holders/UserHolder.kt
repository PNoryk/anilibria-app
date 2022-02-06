package tv.anilibria.module.data.network.datasource.holders

import io.reactivex.Observable
import tv.anilibria.module.data.network.entity.app.other.UserResponse

/**
 * Created by radiationx on 11.01.18.
 */
interface UserHolder {
    fun getUser(): UserResponse

    fun observeUser(): Observable<UserResponse>

    fun saveUser(user: UserResponse)

    fun delete()
}