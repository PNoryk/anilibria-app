package tv.anilibria.plugin.data.network

import okhttp3.FormBody

fun Map<String, String>.toFormBody(): FormBody {
    val builder = FormBody.Builder()
    forEach { builder.add(it.key, it.value) }
    return builder.build()
}

fun formBodyOf(vararg pairs: Pair<String,String>):FormBody = mapOf(*pairs).toFormBody()

