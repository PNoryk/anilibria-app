package ru.radiationx.shared.ktx

import kotlinx.datetime.DayOfWeek
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

private object DateFormats {
    val dateTimeFormat = SimpleDateFormat("dd.MM.yyyy, HH:mm", Locale.getDefault())
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val timeSecFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
    val timeHourMinuteSecFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val timeSecFormatUtc = SimpleDateFormat("mm:ss", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    val timeHourMinuteSecFormatUtc = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
}

fun Date.asDateTimeString(): String = DateFormats.dateTimeFormat.format(this)
fun Date.asDateString(): String = DateFormats.dateFormat.format(this)
fun Date.asTimeString(): String = DateFormats.timeFormat.format(this)
fun Date.asTimeSecString(): String {
    return if (time >= TimeUnit.HOURS.toMillis(1)) {
        DateFormats.timeHourMinuteSecFormatUtc.format(this)
    } else {
        DateFormats.timeSecFormatUtc.format(this)
    }
}

fun Long.asUtcTime(): Long = this - TimeZone.getDefault().rawOffset / 1000
fun Long.asUtc(): Long = this - TimeZone.getDefault().rawOffset

fun Long.asMskTime() = this.asUtcTime() + TimeUnit.HOURS.toSeconds(3)
fun Long.asMsk() = this.asUtc() + TimeUnit.HOURS.toMillis(3)

fun Long.getDayOfWeek() = Calendar.getInstance().also {
    it.timeInMillis = this
}.get(Calendar.DAY_OF_WEEK)

fun DayOfWeek.asDayName() = when (this) {
    DayOfWeek.MONDAY -> "Понедельник"
    DayOfWeek.TUESDAY -> "Вторник"
    DayOfWeek.WEDNESDAY -> "Среда"
    DayOfWeek.THURSDAY -> "Четверг"
    DayOfWeek.FRIDAY -> "Пятница"
    DayOfWeek.SATURDAY -> "Суббота"
    DayOfWeek.SUNDAY -> "Воскресенье"
}

fun DayOfWeek.asDayNameDeclension() = when (this) {
    DayOfWeek.MONDAY -> "Понедельник"
    DayOfWeek.TUESDAY -> "Вторник"
    DayOfWeek.WEDNESDAY -> "Среду"
    DayOfWeek.THURSDAY -> "Четверг"
    DayOfWeek.FRIDAY -> "Пятницу"
    DayOfWeek.SATURDAY -> "Субботу"
    DayOfWeek.SUNDAY -> "Воскресенье"
}

fun DayOfWeek.asDayPretext() = when (this) {
    DayOfWeek.MONDAY -> "в"
    DayOfWeek.TUESDAY -> "во"
    DayOfWeek.WEDNESDAY -> "в"
    DayOfWeek.THURSDAY -> "в"
    DayOfWeek.FRIDAY -> "в"
    DayOfWeek.SATURDAY -> "в"
    DayOfWeek.SUNDAY -> "в"
}

fun Date.isSameDay(date: Date): Boolean {
    val cal1: Calendar = Calendar.getInstance()
    cal1.time = this
    val cal2: Calendar = Calendar.getInstance()
    cal2.time = date
    return cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
            cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}
