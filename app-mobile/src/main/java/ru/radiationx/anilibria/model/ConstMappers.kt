package ru.radiationx.anilibria.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import ru.radiationx.anilibria.R
import tv.anilibria.module.domain.entity.other.DataColor
import tv.anilibria.module.domain.entity.other.DataIcon

@DrawableRes
fun DataIcon.asDataIconRes(): Int? = when (this) {
    DataIcon.VK -> R.drawable.ic_logo_vk
    DataIcon.YOUTUBE -> R.drawable.ic_logo_youtube
    DataIcon.PATREON -> R.drawable.ic_logo_patreon
    DataIcon.TELEGRAM -> R.drawable.ic_logo_telegram
    DataIcon.DISCORD -> R.drawable.ic_logo_discord
    DataIcon.YOOMONEY -> R.drawable.ic_logo_yoomoney
    DataIcon.DONATIONALERTS -> R.drawable.ic_logo_donationalerts
    DataIcon.ANILIBRIA -> R.drawable.ic_anilibria
    DataIcon.INFO -> R.drawable.ic_information
    DataIcon.RULES -> R.drawable.ic_book_open_variant
    DataIcon.PERSON -> R.drawable.ic_person
    DataIcon.SITE -> R.drawable.ic_link
    DataIcon.INFRA -> R.drawable.ic_server_plus
    DataIcon.UNKNOWN -> null
}

@ColorRes
fun DataColor.asDataColorRes(): Int? = when (this) {
    DataColor.VK -> R.color.brand_vk
    DataColor.YOUTUBE -> R.color.brand_youtube
    DataColor.PATREON -> R.color.brand_patreon
    DataColor.TELEGRAM -> R.color.brand_telegram
    DataColor.DISCORD -> R.color.brand_discord
    DataColor.YOOMONEY -> R.color.brand_yoomoney
    DataColor.DONATIONALERTS -> R.color.brand_donationalerts
    DataColor.ANILIBRIA -> R.color.alib_red
    DataColor.UNKNOWN -> null
}