package ru.radiationx.anilibria.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.merge_days_bar.view.*
import kotlinx.datetime.DayOfWeek
import ru.radiationx.anilibria.R

class DaysBar @JvmOverloads constructor(
    context: Context, attrs:
    AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val daysViews: Map<DayOfWeek, View>
    private val buttons: List<View>

    var clickListener: ((day: DayOfWeek) -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.merge_days_bar, this, true)

        daysViews = mapOf(
            DayOfWeek.MONDAY to day1,
            DayOfWeek.TUESDAY to day2,
            DayOfWeek.WEDNESDAY to day3,
            DayOfWeek.THURSDAY to day4,
            DayOfWeek.FRIDAY to day5,
            DayOfWeek.SATURDAY to day6,
            DayOfWeek.SUNDAY to day7
        )
        buttons = daysViews.values.toList()

        buttons.forEach {
            it.setOnClickListener { btn ->
                val day = daysViews.entries.firstOrNull { it.value == btn }?.key
                day?.also {
                    clickListener?.invoke(it)
                }
            }
        }
    }

    fun selectDays(day: DayOfWeek?) {
        buttons.forEach { it.isSelected = false }
        day?.also {
            daysViews[it]?.isSelected = true
        }
    }

}
