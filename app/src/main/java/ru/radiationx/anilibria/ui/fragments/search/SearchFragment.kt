package ru.radiationx.anilibria.ui.fragments.search

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_main_base.*
import kotlinx.android.synthetic.main.fragment_releases.*
import ru.radiationx.anilibria.App
import ru.radiationx.anilibria.R
import ru.radiationx.anilibria.entity.app.release.GenreItem
import ru.radiationx.anilibria.entity.app.release.ReleaseItem
import ru.radiationx.anilibria.entity.app.search.SearchItem
import ru.radiationx.anilibria.presentation.search.SearchPresenter
import ru.radiationx.anilibria.presentation.search.SearchView
import ru.radiationx.anilibria.ui.common.RouterProvider
import ru.radiationx.anilibria.ui.fragments.BaseFragment
import ru.radiationx.anilibria.ui.fragments.SharedProvider
import ru.radiationx.anilibria.ui.fragments.release.list.ReleasesAdapter
import ru.radiationx.anilibria.utils.ToolbarHelper


class SearchFragment : BaseFragment(), SearchView, SharedProvider, ReleasesAdapter.ItemListener {

    companion object {
        const val ARG_QUERY_TEXT: String = "query"
        const val ARG_GENRE: String = "genre"
    }

    private lateinit var searchView: com.lapism.searchview.SearchView
    private lateinit var genresDialog: GenresDialog
    private lateinit var searchMenuItem: MenuItem
    private val adapter = ReleasesAdapter(this)
    private val fastAdapter = FastSearchAdapter()
    private var currentTitle: String? = "Поиск"

    @InjectPresenter
    lateinit var presenter: SearchPresenter

    @ProvidePresenter
    fun provideSearchPresenter(): SearchPresenter {
        return SearchPresenter(App.injections.releaseRepository,
                App.injections.searchRepository,
                (parentFragment as RouterProvider).router)
    }

    override var sharedViewLocal: View? = null

    override fun getSharedView(): View? {
        val sharedView = sharedViewLocal
        sharedViewLocal = null
        return sharedView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            presenter.currentQuery = it.getString(ARG_QUERY_TEXT, null)
            presenter.currentGenre = it.getString(ARG_GENRE, null)
        }
    }

    override fun getLayoutResource(): Int = R.layout.fragment_releases

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        genresDialog = context?.let {
            GenresDialog(it, object : GenresDialog.ClickListener {
                override fun onItemClick(item: GenreItem) {
                    presenter.currentGenre = item.value
                    presenter.refreshReleases()
                }
            })
        } ?: throw RuntimeException("Burn in hell google! Wtf, why nullable?! Fags...")

        refreshLayout.setOnRefreshListener { presenter.refreshReleases() }

        recyclerView.apply {
            adapter = this@SearchFragment.adapter
            layoutManager = LinearLayoutManager(recyclerView.context)
        }

        ToolbarHelper.fixInsets(toolbar)
        with(toolbar) {
            title = currentTitle
            setNavigationOnClickListener({
                presenter.onBackPressed()
            })
            setNavigationIcon(R.drawable.ic_toolbar_arrow_back)
        }

        searchView = com.lapism.searchview.SearchView(toolbar.context)
        toolbar.addView(searchView)
        with(searchView) {
            setNavigationIcon(R.drawable.ic_toolbar_arrow_back)
            setOnOpenCloseListener(object : com.lapism.searchview.SearchView.OnOpenCloseListener {
                override fun onOpen(): Boolean {
                    searchMenuItem.isVisible = false
                    toolbar?.navigationIcon = null
                    toolbar?.title = null
                    return false
                }

                override fun onClose(): Boolean {
                    searchMenuItem.isVisible = true
                    toolbar?.setNavigationIcon(R.drawable.ic_toolbar_arrow_back)
                    toolbar?.title = currentTitle
                    return false
                }

            })
            setVoice(false)
            setShadow(false)
            version = com.lapism.searchview.SearchView.Version.MENU_ITEM
            versionMargins = com.lapism.searchview.SearchView.VersionMargins.MENU_ITEM
            setOnQueryTextListener(object : com.lapism.searchview.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        presenter.currentQuery = it
                        presenter.refreshReleases()
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    //newText?.let { presenter.fastSearch(it) }
                    return false
                }
            })
            hint = "Поиск"
            if (presenter.isEmpty()) {
                open(true)
            }
            //releaseAdapter = fastAdapter
        }


        with(toolbar.menu) {
            searchMenuItem = add("Search")
                    .setIcon(R.drawable.ic_toolbar_search)
                    .setOnMenuItemClickListener {
                        searchView.open(true)
                        false
                    }
                    .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)

            add("Settings")
                    .setIcon(R.drawable.ic_toolbar_settings)
                    .setOnMenuItemClickListener {
                        genresDialog.showDialog()
                        false
                    }
                    .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
        }
    }

    override fun onBackPressed(): Boolean {
        presenter.onBackPressed()
        return true
    }

    override fun setEndless(enable: Boolean) {
        adapter.endless = enable
    }

    override fun showFastItems(items: List<SearchItem>) {
        searchView.showSuggestions()
        items.forEach {
            Log.e("SUKA", "FAST ITEM: ${it.title} : ${it.originalTitle}")
        }
        fastAdapter.bindItems(items)
    }

    override fun showGenres(genres: List<GenreItem>) {
        genresDialog.setItems(genres)
    }

    override fun showReleases(releases: List<ReleaseItem>) {
        currentTitle = if (presenter.currentQuery.orEmpty().isEmpty()) {
            "Поиск"
        } else {
            "Поиск: " + presenter.currentQuery
        }
        toolbar.title = currentTitle
        adapter.bindItems(releases)
        genresDialog.setChecked(presenter.currentGenre.orEmpty())
    }

    override fun insertMore(releases: List<ReleaseItem>) {
        adapter.insertMore(releases)
    }

    override fun onLoadMore() {
        presenter.loadMore()
    }

    override fun setRefreshing(refreshing: Boolean) {
        refreshLayout.isRefreshing = refreshing
    }

    override fun onItemClick(position: Int, view: View) {
        sharedViewLocal = view
    }

    override fun onItemClick(item: ReleaseItem, position: Int) {
        presenter.onItemClick(item)
    }

    override fun onItemLongClick(item: ReleaseItem): Boolean {
        return presenter.onItemLongClick(item)
    }

}
