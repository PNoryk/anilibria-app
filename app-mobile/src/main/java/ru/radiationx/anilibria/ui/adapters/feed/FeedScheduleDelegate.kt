package ru.radiationx.anilibria.ui.adapters.feed

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_feed_schedule.*
import ru.radiationx.anilibria.R
import ru.radiationx.anilibria.extension.dpToPx
import ru.radiationx.anilibria.ui.adapters.FeedScheduleListItem
import ru.radiationx.anilibria.ui.adapters.ListItem
import ru.radiationx.anilibria.ui.common.adapters.AppAdapterDelegate
import ru.radiationx.data.entity.app.feed.ScheduleItem
import ru.radiationx.shared.ktx.android.visible

/**
 * Created by radiationx on 13.01.18.
 */
class FeedScheduleDelegate(
        private val clickListener: (ScheduleItem, View,Int) -> Unit
) : AppAdapterDelegate<FeedScheduleListItem, ListItem, FeedScheduleDelegate.ViewHolder>(
        R.layout.item_feed_schedule,
        { it is FeedScheduleListItem },
        { ViewHolder(it, clickListener) }
) {

    override fun bindData(item: FeedScheduleListItem, holder: ViewHolder) =
            holder.bind(item.item)

    class ViewHolder(
            override val containerView: View,
            private val clickListener: (ScheduleItem, View,Int) -> Unit
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private lateinit var currentItem: ScheduleItem

        init {
            containerView.setOnClickListener {
                clickListener.invoke(currentItem, item_image, adapterPosition)
            }
            val gradientDrawable = GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    intArrayOf(
                            Color.parseColor("#99ffffff"),
                            Color.parseColor("#99ffffff"),
                            Color.parseColor("#00ffffff")
                    )
            ).also {
                it.cornerRadius = 0f
                it.gradientRadius = item_complete.dpToPx(20).toFloat()
                it.gradientType = GradientDrawable.RADIAL_GRADIENT
            }
            item_complete.background = gradientDrawable
        }

        fun bind(item: ScheduleItem) {
            currentItem = item
            item_complete.visible(item.completed)
            ViewCompat.setTransitionName(item_image, "${item.javaClass.simpleName}_${item.releaseItem.id}")
            ImageLoader.getInstance().displayImage(item.releaseItem.poster, item_image)
        }
    }
}