package tv.anilibria.plugin.data.network

import io.reactivex.Single

interface NetworkClient {

    fun get(url: String, args: Map<String, String>): Single<String>
    fun post(url: String, args: Map<String, String>): Single<String>
    fun put(url: String, args: Map<String, String>): Single<String>
    fun delete(url: String, args: Map<String, String>): Single<String>

    fun getFull(url: String, args: Map<String, String>): Single<NetworkResponse>
    fun postFull(url: String, args: Map<String, String>): Single<NetworkResponse>
    fun putFull(url: String, args: Map<String, String>): Single<NetworkResponse>
    fun deleteFull(url: String, args: Map<String, String>): Single<NetworkResponse>

}
