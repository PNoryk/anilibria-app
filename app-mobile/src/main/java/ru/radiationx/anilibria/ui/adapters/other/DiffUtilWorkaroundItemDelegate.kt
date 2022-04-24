package ru.radiationx.anilibria.ui.adapters.other

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.radiationx.anilibria.R
import ru.radiationx.anilibria.ui.adapters.DiffUtilWorkaroundListItem
import ru.radiationx.anilibria.ui.adapters.ListItem
import ru.radiationx.anilibria.ui.common.adapters.AppAdapterDelegate

class DiffUtilWorkaroundItemDelegate :
    AppAdapterDelegate<DiffUtilWorkaroundListItem, ListItem, DiffUtilWorkaroundItemDelegate.ViewHolder>(
        R.layout.item_diffutil_workaround,
        { it is DiffUtilWorkaroundListItem },
        { ViewHolder(it) }
    ) {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
