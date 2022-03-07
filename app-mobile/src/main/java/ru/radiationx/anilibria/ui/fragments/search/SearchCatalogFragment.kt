package ru.radiationx.anilibria.ui.fragments.search

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.lapism.search.behavior.SearchBehavior
import com.lapism.search.internal.SearchLayout
import com.lapism.search.widget.SearchMenuItem
import kotlinx.android.synthetic.main.fragment_list_refresh.*
import kotlinx.android.synthetic.main.fragment_main_base.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.radiationx.anilibria.R
import ru.radiationx.anilibria.extension.disableItemChangeAnimation
import ru.radiationx.anilibria.model.ReleaseItemState
import ru.radiationx.anilibria.presentation.search.*
import ru.radiationx.anilibria.ui.adapters.PlaceholderListItem
import ru.radiationx.anilibria.ui.fragments.BaseFragment
import ru.radiationx.anilibria.ui.fragments.SharedProvider
import ru.radiationx.anilibria.ui.fragments.ToolbarShadowController
import ru.radiationx.anilibria.ui.fragments.release.list.ReleasesAdapter
import ru.radiationx.anilibria.utils.DimensionHelper
import ru.radiationx.shared.ktx.android.putExtra
import ru.radiationx.shared_app.di.injectDependencies
import tv.anilibria.feature.domain.entity.ReleaseGenre
import tv.anilibria.feature.domain.entity.ReleaseSeason
import tv.anilibria.feature.domain.entity.ReleaseYear
import tv.anilibria.feature.domain.entity.SearchForm


class SearchCatalogFragment : BaseFragment(), SearchCatalogView, FastSearchView, SharedProvider,
    ReleasesAdapter.ItemListener {

    companion object {
        private const val ARG_GENRE: String = "genre"

        fun newInstance(
            genre: String? = null,
        ) = SearchCatalogFragment().putExtra {
            putString(ARG_GENRE, genre)
        }
    }

    private lateinit var genresDialog: GenresDialog
    private val adapter = SearchAdapter(
        loadMoreListener = { presenter.loadMore() },
        loadRetryListener = { presenter.loadMore() },
        listener = this,
        remindCloseListener = { presenter.onRemindClose() },
        emptyPlaceHolder = PlaceholderListItem(
            R.drawable.ic_toolbar_search,
            R.string.placeholder_title_nodata_base,
            R.string.placeholder_desc_nodata_search
        ),
        errorPlaceHolder = PlaceholderListItem(
            R.drawable.ic_toolbar_search,
            R.string.placeholder_title_errordata_base,
            R.string.placeholder_desc_nodata_base
        )
    )

    private val fastSearchAdapter = FastSearchAdapter(
        clickListener = { searchPresenter.onItemClick(it) },
        localClickListener = { searchPresenter.onLocalItemClick(it) }
    )
    private var searchView: SearchMenuItem? = null

    @InjectPresenter
    lateinit var searchPresenter: FastSearchPresenter

    @ProvidePresenter
    fun provideSearchPresenter(): FastSearchPresenter =
        getDependency(FastSearchPresenter::class.java, screenScope)

    @InjectPresenter
    lateinit var presenter: SearchPresenter

    @ProvidePresenter
    fun providePresenter(): SearchPresenter =
        getDependency(SearchPresenter::class.java, screenScope)

    override var sharedViewLocal: View? = null

    override fun getSharedView(): View? {
        val sharedView = sharedViewLocal
        sharedViewLocal = null
        return sharedView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies(screenScope)
        super.onCreate(savedInstanceState)
        arguments?.also { bundle ->
            bundle.getString(ARG_GENRE)?.also { genre ->
                presenter.onChangeGenres(listOf(ReleaseGenre(genre)))
            }
        }
    }

    override fun getLayoutResource(): Int = R.layout.fragment_list_refresh

    override val statusBarVisible: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchView = SearchMenuItem(coordinator_layout.context)
        genresDialog = context?.let {
            GenresDialog(it, object : GenresDialog.ClickListener {
                override fun onAccept() {
                    presenter.onAcceptDialog()
                }

                override fun onClose() {
                    presenter.onCloseDialog()
                }

                override fun onCheckedGenres(items: List<ReleaseGenre>) {
                    Log.e("lululu", "onCheckedItems ${items.size}")
                    presenter.onChangeGenres(items)
                }

                override fun onCheckedYears(items: List<ReleaseYear>) {
                    presenter.onChangeYears(items)
                }

                override fun onCheckedSeasons(items: List<ReleaseSeason>) {
                    presenter.onChangeSeasons(items)
                }

                override fun onChangeSorting(sorting: SearchForm.Sort) {
                    presenter.onChangeSorting(sorting)
                }

                override fun onChangeComplete(complete: Boolean) {
                    presenter.onChangeComplete(complete)
                }
            })
        } ?: throw RuntimeException("Burn in hell google! Wtf, why nullable?! Fags...")

        refreshLayout.setOnRefreshListener { presenter.refreshReleases() }

        recyclerView.apply {
            adapter = this@SearchCatalogFragment.adapter
            layoutManager = LinearLayoutManager(this.context)
            disableItemChangeAnimation()
        }

        ToolbarShadowController(
            recyclerView,
            appbarLayout
        ) {
            updateToolbarShadow(it)
        }

        //ToolbarHelper.fixInsets(toolbar)
        with(toolbar) {
            title = "Поиск"
            /*setNavigationOnClickListener({ presenter.onBackPressed() })
            setNavigationIcon(R.drawable.ic_toolbar_arrow_back)*/
        }

        toolbar.menu.apply {
            add("Поиск")
                .setIcon(R.drawable.ic_toolbar_search)
                .setOnMenuItemClickListener {
                    presenter.onFastSearchClick()
                    searchView?.requestFocus(it)
                    false
                }
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
            add("Фильтры")
                .setIcon(R.drawable.ic_filter_toolbar)
                .setOnMenuItemClickListener {
                    presenter.showDialog()
                    false
                }
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
        }


        coordinator_layout.addView(searchView)
        searchView?.layoutParams =
            (searchView?.layoutParams as androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams?)?.apply {
                width =
                    androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams.MATCH_PARENT
                height =
                    androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams.WRAP_CONTENT
                behavior = SearchBehavior<SearchMenuItem>()
            }
        (searchView as SearchLayout?)?.apply {
            setTextHint("Название релиза")
            setOnFocusChangeListener(object : SearchLayout.OnFocusChangeListener {
                override fun onFocusChange(hasFocus: Boolean) {
                    if (!hasFocus) {
                        searchPresenter.onClose()
                    } else {
                        presenter.onFastSearchOpen()
                    }
                }

            })
            setOnQueryTextListener(object : SearchLayout.OnQueryTextListener {
                override fun onQueryTextSubmit(query: CharSequence): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: CharSequence): Boolean {
                    searchPresenter.onQueryChange(newText.toString())
                    return false
                }
            })

            setAdapter(fastSearchAdapter)
        }
    }

    override fun updateDimens(dimensions: DimensionHelper.Dimensions) {
        super.updateDimens(dimensions)
        searchView?.layoutParams =
            (searchView?.layoutParams as androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams?)?.apply {
                topMargin = dimensions.statusBar
            }
        searchView?.requestLayout()
    }

    override fun onBackPressed(): Boolean {
        presenter.onBackPressed()
        return true
    }

    override fun showState(state: FastSearchScreenState) {
        fastSearchAdapter.bindItems(state)
    }

    override fun showDialog() {
        genresDialog.showDialog()
    }

    override fun showGenres(genres: List<ReleaseGenre>) {
        genresDialog.setItems(genres)
    }

    override fun showYears(years: List<ReleaseYear>) {
        genresDialog.setYears(years)
    }

    override fun showSeasons(seasons: List<ReleaseSeason>) {
        genresDialog.setSeasons(seasons)
    }

    override fun selectGenres(genres: List<ReleaseGenre>) {
        genresDialog.setCheckedGenres(genres)
    }

    override fun selectYears(years: List<ReleaseYear>) {
        genresDialog.setCheckedYears(years)
    }

    override fun selectSeasons(seasons: List<ReleaseSeason>) {
        genresDialog.setCheckedSeasons(seasons)
    }

    override fun setSorting(sorting: SearchForm.Sort) {
        genresDialog.setSorting(sorting)
    }

    override fun setComplete(complete: Boolean) {
        genresDialog.setComplete(complete)
    }

    override fun updateInfo(sort: SearchForm.Sort, filters: Int) {
        var subtitle = ""
        subtitle += when (sort) {
            SearchForm.Sort.DATE -> "По новизне"
            SearchForm.Sort.RATING -> "По популярности"
        }
        subtitle += ", Фильтров: $filters"
        toolbar.subtitle = subtitle
    }

    override fun showState(state: SearchScreenState) {
        progressBarList.isVisible = state.data.emptyLoading
        refreshLayout.isRefreshing = state.data.refreshLoading
        adapter.bindState(state)
    }

    override fun onItemClick(position: Int, view: View) {
        sharedViewLocal = view
    }

    override fun onItemClick(item: ReleaseItemState, position: Int) {
        presenter.onItemClick(item)
    }

    override fun onItemLongClick(item: ReleaseItemState): Boolean {
        context?.let {
            val titles = arrayOf("Копировать ссылку", "Поделиться", "Добавить на главный экран")
            AlertDialog.Builder(it)
                .setItems(titles) { _, which ->
                    when (which) {
                        0 -> {
                            presenter.onCopyClick(item)
                            Toast.makeText(context, "Ссылка скопирована", Toast.LENGTH_SHORT).show()
                        }
                        1 -> presenter.onShareClick(item)
                        2 -> presenter.onShortcutClick(item)
                    }
                }
                .show()
        }
        return false
    }

}
