package ru.radiationx.anilibria.ui.common.adapters

import androidx.recyclerview.widget.DiffUtil
import ru.radiationx.anilibria.ui.adapters.DiffUtilWorkaroundListItem
import ru.radiationx.anilibria.ui.adapters.ListItem
import ru.radiationx.anilibria.ui.adapters.other.DiffUtilWorkaroundItemDelegate

open class ListItemAdapter(
    private val enableDiffUtilWorkaround: Boolean
) : OptimizeAdapter<ListItem>(ListItemDiffCallback) {

    init {
        setHasStableIds(true)
        addDelegate(DiffUtilWorkaroundItemDelegate())
    }

    /*
    * https://medium.com/@dmstocking/how-one-linearlayoutmanager-bug-makes-diffutil-worthless-372a5b887dec
    * https://issuetracker.google.com/issues/73050491
    * */
    private val workaroundItems = if (enableDiffUtilWorkaround) {
        listOf(DiffUtilWorkaroundListItem("WORKAROUND"))
    } else {
        emptyList()
    }

    final override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(hasStableIds)
    }

    override fun getItemId(position: Int): Long {
        return items[position].getItemId()
    }

    override fun setItems(items: MutableList<ListItem>?) {
        if (enableDiffUtilWorkaround) {
            super.setItems(workaroundItems + items.orEmpty())
        } else {
            super.setItems(items)
        }
    }

    override fun setItems(items: MutableList<ListItem>?, commitCallback: Runnable?) {
        if (enableDiffUtilWorkaround) {
            super.setItems(workaroundItems + items.orEmpty(), commitCallback)
        } else {
            super.setItems(items, commitCallback)
        }
    }
}

object ListItemDiffCallback : DiffUtil.ItemCallback<ListItem>() {

    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return oldItem.getItemId() == newItem.getItemId()
    }

    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return oldItem.getItemHash() == newItem.getItemHash()
    }

    override fun getChangePayload(oldItem: ListItem, newItem: ListItem): Any? {
        return newItem.getPayloadBy(oldItem)
    }
}