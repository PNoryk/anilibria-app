package anilibria.tv.storage.impl.entity


import anilibria.tv.domain.entity.user.User
import com.google.gson.annotations.SerializedName

data class UserAuthStorage(
    @SerializedName("state") val state: Int,
    @SerializedName("user") val user: UserStorage?
){

    companion object{
        const val STATE_NO_AUTH = 1
        const val STATE_AUTH = 2
    }
}