package tv.anilibria.module.data.repos

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import tv.anilibria.module.data.local.holders.UserLocalDataSource
import tv.anilibria.module.data.restapi.datasource.remote.api.UserRemoteDataSource
import tv.anilibria.module.domain.entity.other.User
import javax.inject.Inject

/**
 * Created by radiationx on 30.12.17.
 */
class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userHolder: UserLocalDataSource,
) {

    fun observeUser(): Flow<User> {
        return userHolder.observe().filterNotNull()
    }

    suspend fun loadUser(): User {
        return userRemoteDataSource.loadUser().also {
            userHolder.put(it)
        }
    }

}