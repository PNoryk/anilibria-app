package tv.anilibria.feature.user.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import toothpick.InjectConstructor
import tv.anilibria.feature.user.data.domain.User
import tv.anilibria.feature.user.data.local.UserLocalDataSource
import tv.anilibria.feature.user.data.remote.UserRemoteDataSource

/**
 * Created by radiationx on 30.12.17.
 */
@InjectConstructor
class UserRepository(
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