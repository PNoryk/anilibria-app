package tv.anilibria.module.data.network.datasource.storage

import android.content.SharedPreferences
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import org.json.JSONObject
import tv.anilibria.module.data.network.datasource.holders.UserHolder
import tv.anilibria.module.data.network.entity.app.other.UserResponse
import tv.anilibria.module.data.network.entity.common.AuthState
import javax.inject.Inject

/**
 * Created by radiationx on 11.01.18.
 */
class UserStorage @Inject constructor(
        private val sharedPreferences: SharedPreferences
) : UserHolder {

    private val userRelay = BehaviorRelay.createDefault(getSavedUser())

    override fun getUser(): UserResponse = userRelay.value!!

    private fun getSavedUser(): UserResponse {
        val user = UserResponse()
        val userSource = sharedPreferences.getString("saved_user", null)
        if (userSource == null) {
            user.authState = AuthState.NO_AUTH
            localSaveUser(user)
        } else {
            val userJson = JSONObject(userSource)
            user.apply {
                authState = AuthState.valueOf(userJson.getString("authState"))
                id = userJson.getInt("id")
                nick = userJson.getString("nick")
                avatarUrl = userJson.getString("avatar")
            }
        }
        return user
    }

    private fun localSaveUser(user: UserResponse) {
        val userJson = JSONObject()
        userJson.put("authState", user.authState.toString())
        userJson.put("id", user.id)
        userJson.put("nick", user.nick)
        userJson.put("avatar", user.avatarUrl)
        sharedPreferences.edit().putString("saved_user", userJson.toString()).apply()
    }

    override fun observeUser(): Observable<UserResponse> = userRelay

    override fun saveUser(user: UserResponse) {
        localSaveUser(user)
        userRelay.accept(user)
    }

    override fun delete() {
        val user = getUser()
        user.apply {
            authState = AuthState.AUTH_SKIPPED
            id = UserResponse.NO_ID
            nick = UserResponse.NO_VALUE
            avatarUrl = UserResponse.NO_VALUE
        }
        saveUser(user)
    }

}