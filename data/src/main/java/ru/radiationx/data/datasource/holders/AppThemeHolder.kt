package ru.radiationx.data.datasource.holders

import io.reactivex.Observable

@Deprecated("old data")
interface AppThemeHolder {
    fun observeTheme(): Observable<AppTheme>
    fun getTheme(): AppTheme
    enum class AppTheme { LIGHT, DARK }
}