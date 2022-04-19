package ru.radiationx.anilibria.ui.activities.updatechecker

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Html
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_updater.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import ru.radiationx.anilibria.R
import ru.radiationx.shared.ktx.android.getCompatColor
import ru.radiationx.anilibria.presentation.checker.CheckerPresenter
import ru.radiationx.anilibria.presentation.checker.CheckerView
import ru.radiationx.anilibria.ui.activities.BaseActivity
import ru.radiationx.shared.ktx.android.gone
import ru.radiationx.shared.ktx.android.visible
import ru.radiationx.shared_app.AppLinkHelper
import ru.radiationx.shared_app.common.SystemUtils
import ru.radiationx.shared_app.di.getDependency
import ru.radiationx.shared_app.di.injectDependencies
import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.core.types.HtmlText
import tv.anilibria.feature.appupdates.data.domain.UpdateData
import tv.anilibria.feature.appupdates.data.domain.UpdateLink
import tv.anilibria.feature.appupdates.data.domain.UpdateLinkType
import tv.anilibria.feature.analytics.api.features.UpdaterAnalytics
import tv.anilibria.plugin.data.analytics.LifecycleTimeCounter
import tv.anilibria.plugin.shared.appinfo.SharedBuildConfig
import javax.inject.Inject

/**
 * Created by radiationx on 24.07.17.
 */

@RuntimePermissions
class UpdateCheckerActivity : BaseActivity(), CheckerView {

    companion object {
        private const val ARG_FORCE = "force"
        private const val ARG_ANALYTICS_FROM = "from"

        fun newIntent(context: Context, force: Boolean, analyticsFrom: String) =
            Intent(context, UpdateCheckerActivity::class.java).apply {
                putExtra(ARG_FORCE, true)
                putExtra(ARG_ANALYTICS_FROM, analyticsFrom)
                action = Intent.ACTION_VIEW
            }
    }

    private val useTimeCounter by lazy {
        LifecycleTimeCounter(presenter::submitUseTime)
    }

    @Inject
    lateinit var sharedBuildConfig: SharedBuildConfig

    @Inject
    lateinit var updaterAnalytics: UpdaterAnalytics

    @Inject
    lateinit var appLinkHelper: AppLinkHelper

    @Inject
    lateinit var systemUtils: SystemUtils

    @InjectPresenter
    lateinit var presenter: CheckerPresenter

    @ProvidePresenter
    fun provideCheckerPresenter() = getDependency(CheckerPresenter::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_updater)
        lifecycle.addObserver(useTimeCounter)

        intent?.let {
            presenter.forceLoad = it.getBooleanExtra(ARG_FORCE, false)
            it.getStringExtra(ARG_ANALYTICS_FROM)?.also {
                updaterAnalytics.open(it)
            }
        }
        presenter.checkUpdate()

        toolbar.setNavigationOnClickListener { finish() }
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow_back)

        currentInfo.text =
            generateCurrentInfo(sharedBuildConfig.versionName, sharedBuildConfig.buildDate)
    }

    override fun showUpdateData(update: UpdateData) {
        val currentVersionCode = sharedBuildConfig.versionCode

        if (update.code > currentVersionCode) {
            updateInfo.text = generateCurrentInfo(update.name, update.date)
            addSection("Важно", update.important)
            addSection("Добавлено", update.added)
            addSection("Исправлено", update.fixed)
            addSection("Изменено", update.changed)

            updateInfo.visible()
            updateButton.visible()
            divider.visible()
        } else {
            updateInfo.text = "Обновлений нет, но вы можете загрузить текущую версию еще раз"
            updateInfo.visible()
            updateContent.gone()
            divider.gone()
        }
        updateButton.visible()
        updateButton.setOnClickListener {
            presenter.onDownloadClick()
            openDownloadDialog(update)
        }
    }

    private fun openDownloadDialog(update: UpdateData) {
        if (update.links.isEmpty()) {
            return
        }
        if (update.links.size == 1) {
            val link = update.links.last()
            presenter.onSourceDownloadClick(link.name)
            decideDownload(link)
            return
        }
        val titles = update.links.map { it.name }.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle("Источник")
            .setItems(titles) { _, which ->
                //Utils.externalLink(update.links[titles[which]].orEmpty())
                val link = update.links[which]
                presenter.onSourceDownloadClick(link.name)
                decideDownload(link)
            }
            .show()
    }

    private fun decideDownload(link: UpdateLink) {
        when (link.type) {
            UpdateLinkType.FILE -> systemDownloadWithPermissionCheck(link.url)
            UpdateLinkType.SITE -> appLinkHelper.openLink(link.url)
            UpdateLinkType.UNKNOWN -> appLinkHelper.openLink(link.url)
        }
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun systemDownload(url: AbsoluteUrl) {
        systemUtils.systemDownloader(url.value)
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun setRefreshing(isRefreshing: Boolean) {
        progressBar.visible(isRefreshing)
        updateInfo.gone(isRefreshing)
        updateContent.gone(isRefreshing)
        updateButton.gone(isRefreshing)
        divider.gone(isRefreshing)
    }

    private fun addSection(title: String, array: List<HtmlText>) {
        if (array.isEmpty()) {
            return
        }
        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.setPadding(0, 0, 0, (resources.displayMetrics.density * 24).toInt())

        val sectionTitle = TextView(this)
        sectionTitle.text = title
        sectionTitle.setPadding(0, 0, 0, (resources.displayMetrics.density * 8).toInt())
        sectionTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
        sectionTitle.setTextColor(getCompatColor(R.color.light_textDefault))
        root.addView(sectionTitle)

        val stringBuilder = StringBuilder()

        array.forEachIndexed { index, s ->
            stringBuilder.append("— ").append(s.text)
            if (index + 1 < array.size) {
                stringBuilder.append("<br>")
            }
        }

        val sectionText = TextView(this)
        sectionText.text = Html.fromHtml(stringBuilder.toString())
        sectionText.setPadding((resources.displayMetrics.density * 8).toInt(), 0, 0, 0)
        sectionText.setTextColor(getCompatColor(R.color.light_textDefault))
        root.addView(sectionText)

        updateContent.addView(
            root,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
    }

    private fun generateCurrentInfo(name: String?, date: String?): String {
        return String.format("Версия: %s\nСборка от: %s", name, date)
    }
}
