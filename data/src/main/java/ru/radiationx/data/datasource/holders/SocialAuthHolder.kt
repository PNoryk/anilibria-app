package ru.radiationx.data.datasource.holders

import io.reactivex.Observable
import ru.radiationx.data.entity.app.auth.SocialAuth

@Deprecated("old data")
interface SocialAuthHolder {
    fun get(): List<SocialAuth>
    fun observe(): Observable<List<SocialAuth>>
    fun save(items: List<SocialAuth>)
    fun delete()
}