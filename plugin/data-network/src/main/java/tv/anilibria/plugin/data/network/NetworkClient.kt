package tv.anilibria.plugin.data.network

import io.reactivex.Single

interface NetworkClient {
    fun get(url: String, args: Map<String, String>): Single<NetworkResponse>
    fun post(url: String, args: Map<String, String>): Single<NetworkResponse>
    fun put(url: String, args: Map<String, String>): Single<NetworkResponse>
    fun delete(url: String, args: Map<String, String>): Single<NetworkResponse>
}
