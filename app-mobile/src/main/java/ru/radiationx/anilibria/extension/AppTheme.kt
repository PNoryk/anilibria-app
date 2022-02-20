package ru.radiationx.anilibria.extension

import androidx.annotation.StyleRes
import biz.source_code.miniTemplator.MiniTemplator
import ru.radiationx.anilibria.R
import tv.anilibria.module.data.preferences.AppTheme


@StyleRes
fun AppTheme.getMainStyleRes() = when (this) {
    AppTheme.LIGHT -> R.style.LightAppTheme_NoActionBar
    AppTheme.DARK -> R.style.DarkAppTheme_NoActionBar
}

@StyleRes
fun AppTheme.getPrefStyleRes() = when (this) {
    AppTheme.LIGHT -> R.style.PreferencesLightAppTheme
    AppTheme.DARK -> R.style.PreferencesDarkAppTheme
}

fun AppTheme.getWebStyleType() = when (this) {
    AppTheme.LIGHT -> "light"
    AppTheme.DARK -> "dark"
}

fun AppTheme.isDark() = when (this) {
    AppTheme.LIGHT -> false
    AppTheme.DARK -> true
}

fun MiniTemplator.generateWithTheme(appTheme: AppTheme): String {
    this.setVariableOpt("app_theme", appTheme.getWebStyleType())
    return generateOutput().also {
        reset()
    }
}