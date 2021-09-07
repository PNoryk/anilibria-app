package ru.radiationx.anilibria.ui.fragments.search

import ru.radiationx.anilibria.ui.adapters.PlaceholderListItem
import ru.radiationx.anilibria.ui.adapters.ReleaseListItem
import ru.radiationx.anilibria.ui.adapters.ReleaseRemindListItem
import ru.radiationx.anilibria.ui.adapters.release.detail.ReleaseRemindDelegate
import ru.radiationx.anilibria.ui.fragments.release.list.ReleasesAdapter
import ru.radiationx.data.datasource.holders.PreferencesHolder
import ru.radiationx.data.entity.app.release.ReleaseItem
import ru.radiationx.shared_app.di.DI

/**
 * Created by radiationx on 04.03.18.
 */
class SearchAdapter(
    listener: ItemListener,
    placeholder: PlaceholderListItem
) : ReleasesAdapter(listener, placeholder) {

    private val remindText =
        "Если не удаётся найти нужный релиз, попробуйте искать через Google или Yandex c приставкой \"AniLibria\".\nПо ссылке в поисковике можно будет открыть приложение."

    private val appPreferences = DI.get(PreferencesHolder::class.java)

    private val remindCloseListener = object : ReleaseRemindDelegate.Listener {
        override fun onClickClose(position: Int) {
            localItems.removeAt(position)
            notifyItemRangeRemoved(position, 1)
            appPreferences.setSearchRemind(false)
        }
    }

    init {
        delegatesManager.run {
            addDelegate(ReleaseRemindDelegate(remindCloseListener))
        }
    }

    override fun bindItems(newItems: List<ReleaseItem>) {
        localItems.clear()
        if (newItems.isEmpty() && appPreferences.getSearchRemind()) {
            localItems.add(ReleaseRemindListItem(remindText))
        }
        localItems.addAll(newItems.map { ReleaseListItem(it) })
        updatePlaceholder(newItems.isEmpty())
        addLoadMore()
        notifyDiffItems()
    }
}