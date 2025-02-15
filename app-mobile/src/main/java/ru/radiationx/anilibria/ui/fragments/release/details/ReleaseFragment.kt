package ru.radiationx.anilibria.ui.fragments.release.details

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_main_base.*
import kotlinx.android.synthetic.main.fragment_paged.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.radiationx.anilibria.R
import ru.radiationx.anilibria.presentation.release.details.ReleasePresenter
import ru.radiationx.anilibria.presentation.release.details.ReleaseView
import ru.radiationx.anilibria.ui.fragments.BaseFragment
import ru.radiationx.anilibria.ui.fragments.SharedReceiver
import ru.radiationx.anilibria.ui.fragments.comments.LazyVkCommentsFragment
import ru.radiationx.anilibria.ui.widgets.ScrimHelper
import ru.radiationx.anilibria.ui.widgets.UILImageListener
import ru.radiationx.anilibria.utils.ShortcutHelper
import ru.radiationx.anilibria.utils.ToolbarHelper
import ru.radiationx.anilibria.utils.Utils
import ru.radiationx.data.analytics.features.CommentsAnalytics
import ru.radiationx.data.entity.app.release.ReleaseItem
import ru.radiationx.shared.ktx.android.gone
import ru.radiationx.shared.ktx.android.putExtra
import ru.radiationx.shared.ktx.android.visible
import ru.radiationx.shared_app.di.injectDependencies
import javax.inject.Inject


/* Created by radiationx on 16.11.17. */
open class ReleaseFragment : BaseFragment(), ReleaseView, SharedReceiver {
    companion object {
        private const val ARG_ID: String = "release_id"
        private const val ARG_ID_CODE: String = "release_id_code"
        private const val ARG_ITEM: String = "release_item"
        const val TRANSACTION = "CHTO_TEBE_SUKA_NADO_ESHO"

        fun newInstance(
            id: Int = -1,
            code: String? = null,
            item: ReleaseItem? = null
        ) = ReleaseFragment().putExtra {
            putInt(ARG_ID, id)
            putString(ARG_ID_CODE, code)
            putSerializable(ARG_ITEM, item)
        }
    }

    override val needToolbarShadow: Boolean = false

    private val pagerAdapter: CustomPagerAdapter by lazy { CustomPagerAdapter() }
    private var currentColor: Int = Color.TRANSPARENT
    private var currentTitle: String? = null
    private var toolbarHelperDisposable: Disposable = Disposables.disposed()

    private val defaultOptionsUIL: DisplayImageOptions.Builder = DisplayImageOptions.Builder()
        .cacheInMemory(true)
        .resetViewBeforeLoading(false)
        .cacheInMemory(true)
        .cacheOnDisk(true)
        .displayer(FadeInBitmapDisplayer(1000, true, false, false))
        .bitmapConfig(Bitmap.Config.ARGB_8888)

    @Inject
    lateinit var commentsAnalytics: CommentsAnalytics

    @InjectPresenter
    lateinit var presenter: ReleasePresenter

    @ProvidePresenter
    fun provideReleasePresenter(): ReleasePresenter =
        getDependency(ReleasePresenter::class.java, screenScope)

    override var transitionNameLocal = ""

    override fun setTransitionName(name: String) {
        transitionNameLocal = name
    }

    override fun getLayoutResource(): Int = R.layout.fragment_paged

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies(screenScope)
        super.onCreate(savedInstanceState)
        Log.e("S_DEF_LOG", "ONCRETE $this")
        Log.e("S_DEF_LOG", "ONCRETE REL $arguments, $savedInstanceState")
        arguments?.also { bundle ->
            presenter.releaseId = bundle.getInt(ARG_ID, presenter.releaseId)
            presenter.releaseIdCode = bundle.getString(ARG_ID_CODE, presenter.releaseIdCode)
            presenter.argReleaseItem = bundle.getSerializable(ARG_ITEM) as ReleaseItem?
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.d("kukeki", "onViewCreated $transitionNameLocal")
            toolbarImage.transitionName = transitionNameLocal
        }
        postponeEnterTransition()
        ToolbarHelper.setTransparent(toolbar, appbarLayout)
        ToolbarHelper.setScrollFlag(
            toolbarLayout,
            AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED
        )
        ToolbarHelper.fixInsets(toolbar)
        ToolbarHelper.marqueeTitle(toolbar)

        toolbar.apply {
            currentTitle = getString(R.string.fragment_title_release)
            setNavigationOnClickListener {
                presenter.onBackPressed()
            }
            setNavigationIcon(R.drawable.ic_toolbar_arrow_back)
            menu.add("Копировать ссылку")
                .setOnMenuItemClickListener {
                    presenter.onCopyLinkClick()
                    false
                }

            menu.add("Поделиться")
                .setOnMenuItemClickListener {
                    presenter.onShareClick()
                    false
                }

            menu.add("Добавить на главный экран")
                .setOnMenuItemClickListener {
                    presenter.onShortcutAddClick()
                    false
                }
        }
        toolbarInsetShadow.visible()
        toolbarImage.visible()



        toolbarImage.maxHeight = (resources.displayMetrics.heightPixels * 0.75f).toInt()

        val scrimHelper = ScrimHelper(appbarLayout, toolbarLayout)
        scrimHelper.setScrimListener(object : ScrimHelper.ScrimListener {
            override fun onScrimChanged(scrim: Boolean) {
                toolbarInsetShadow.gone(scrim)
                if (scrim) {
                    toolbar?.let {
                        it.navigationIcon?.clearColorFilter()
                        it.overflowIcon?.clearColorFilter()
                        it.title = currentTitle
                    }
                } else {
                    toolbar?.let {
                        it.navigationIcon?.setColorFilter(currentColor, PorterDuff.Mode.SRC_ATOP)
                        it.overflowIcon?.setColorFilter(currentColor, PorterDuff.Mode.SRC_ATOP)
                        it.title = null
                    }
                }
            }
        })

        viewPagerPaged.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                if (position == 1) {
                    appbarLayout.setExpanded(false)
                    presenter.onCommentsSwipe()
                }
            }
        })

        viewPagerPaged.adapter = pagerAdapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(ARG_ID, presenter.releaseId)
        outState.putString(ARG_ID_CODE, presenter.releaseIdCode)
    }

    override fun onBackPressed(): Boolean {
        if (viewPagerPaged.currentItem > 0) {
            viewPagerPaged.currentItem = viewPagerPaged.currentItem - 1
            return true
        }
        presenter.onBackPressed()
        return true
    }

    override fun setRefreshing(refreshing: Boolean) {
        progressBarPaged.visible(refreshing)
    }

    override fun showState(state: ReleasePagerState) {
        if (state.poster == null) {
            startPostponedEnterTransition()
        } else {
            ImageLoader.getInstance().displayImage(
                state.poster,
                toolbarImage,
                defaultOptionsUIL.build(),
                imageListener
            )
        }

        if (state.title != null) {
            currentTitle = state.title
        }
    }

    override fun shareRelease(text: String) {
        Utils.shareText(text)
    }

    override fun copyLink(url: String) {
        Utils.copyToClipBoard(url)
        Toast.makeText(context, "Ссылка скопирована", Toast.LENGTH_SHORT).show()
    }

    override fun addShortCut(release: ReleaseItem) {
        ShortcutHelper.addShortcut(release)
    }

    override fun onDestroyView() {
        toolbarHelperDisposable.dispose()
        super.onDestroyView()
    }

    private val imageListener = object : UILImageListener() {
        override fun onLoadingStarted(imageUri: String?, view: View?) {
            super.onLoadingStarted(imageUri, view)
            toolbarImageProgress?.visible()
        }

        override fun onLoadingFinally(imageUrl: String?, view: View?) {
            toolbarImageProgress?.gone()
            startPostponedEnterTransition()
        }

        override fun onLoadingComplete(
            imageUri: String?,
            view: View?,
            loadedImage: Bitmap
        ) {
            super.onLoadingComplete(imageUri, view, loadedImage)
            updateToolbarColors(loadedImage)
        }
    }

    private fun updateToolbarColors(loadedImage: Bitmap) {
        toolbarHelperDisposable.dispose()
        toolbarHelperDisposable = ToolbarHelper.isDarkImage(loadedImage, Consumer {
            currentColor = if (it) Color.WHITE else Color.BLACK

            toolbar.navigationIcon?.setColorFilter(
                currentColor,
                PorterDuff.Mode.SRC_ATOP
            )
            toolbar.overflowIcon?.setColorFilter(
                currentColor,
                PorterDuff.Mode.SRC_ATOP
            )
        })
    }

    private inner class CustomPagerAdapter :
        androidx.fragment.app.FragmentStatePagerAdapter(
            childFragmentManager,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {

        private val fragments = listOf<Fragment>(
            ReleaseInfoFragment(),
            LazyVkCommentsFragment()
        )

        init {
            fragments.forEach {
                val newBundle = (this@ReleaseFragment.arguments?.clone() as Bundle?)
                it.arguments = newBundle
                it.putExtra {
                    putString(ARG_SCREEN_SCOPE, screenScope)
                }
                Log.e("lalallala", "CustomPagerAdapter ini $newBundle")
            }
        }

        override fun getItem(position: Int): Fragment = fragments[position]

        override fun getCount(): Int = fragments.size

    }
}
