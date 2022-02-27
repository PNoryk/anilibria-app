package ru.radiationx.anilibria.di

import android.content.Context
import android.text.format.Formatter
import androidx.fragment.app.FragmentActivity
import ru.radiationx.anilibria.common.CardsDataConverter
import ru.radiationx.anilibria.common.DetailDataConverter
import ru.radiationx.shared_app.common.SystemUtils
import toothpick.config.Module

class ActivityModule(activity: FragmentActivity) : Module() {

    init {
        bind(Context::class.java).toInstance(activity)
        bind(SystemUtils::class.java).singleton()
        bind(CardsDataConverter::class.java).singleton()
        bind(DetailDataConverter::class.java).singleton()
        Formatter.formatFileSize(activity, 0L)
    }
}