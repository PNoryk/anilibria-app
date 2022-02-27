package ru.radiationx.anilibria.ui.fragments.release.details

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.dialog_file_download.view.*
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.datetime.DayOfWeek
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import ru.radiationx.anilibria.R
import ru.radiationx.anilibria.extension.disableItemChangeAnimation
import ru.radiationx.anilibria.presentation.release.details.ReleaseDetailScreenState
import ru.radiationx.anilibria.presentation.release.details.ReleaseEpisodeItemState
import ru.radiationx.anilibria.presentation.release.details.ReleaseInfoPresenter
import ru.radiationx.anilibria.presentation.release.details.ReleaseInfoView
import ru.radiationx.anilibria.ui.activities.MyPlayerActivity
import ru.radiationx.anilibria.ui.activities.WebPlayerActivity
import ru.radiationx.anilibria.ui.adapters.release.detail.EpisodeControlPlace
import ru.radiationx.anilibria.ui.adapters.release.detail.ReleaseEpisodeControlDelegate
import ru.radiationx.anilibria.ui.adapters.release.detail.ReleaseEpisodeDelegate
import ru.radiationx.anilibria.ui.adapters.release.detail.ReleaseHeadDelegate
import ru.radiationx.anilibria.ui.fragments.BaseFragment
import ru.radiationx.shared_app.AppLinkHelper
import ru.radiationx.shared_app.common.SystemUtils
import ru.radiationx.shared_app.di.injectDependencies
import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.core.types.RelativeUrl
import tv.anilibria.module.data.analytics.features.mapper.toAnalyticsPlayer
import tv.anilibria.module.data.analytics.features.mapper.toAnalyticsQuality
import tv.anilibria.module.data.preferences.PrefferedPlayerQuality
import tv.anilibria.module.data.preferences.PrefferedPlayerType
import tv.anilibria.module.domain.entity.release.Episode
import tv.anilibria.module.domain.entity.release.Release
import java.net.URLConnection
import java.util.regex.Pattern
import javax.inject.Inject

@RuntimePermissions
class ReleaseInfoFragment : BaseFragment(), ReleaseInfoView {

    companion object {
        const val ARG_ID: String = "release_id"
        const val ARG_ID_CODE: String = "release_id_code"
    }

    private val releaseInfoAdapter: ReleaseInfoAdapter by lazy {
        ReleaseInfoAdapter(
            headListener = headListener,
            episodeListener = episodeListener,
            episodeControlListener = episodeControlListener,
            donationListener = { presenter.onClickDonate() },
            donationCloseListener = {},
            torrentClickListener = presenter::onTorrentClick,
            commentsClickListener = presenter::onCommentsClick,
            episodesTabListener = presenter::onEpisodeTabClick,
            remindCloseListener = presenter::onRemindCloseClick
        )
    }

    @Inject
    lateinit var systemUtils: SystemUtils

    @Inject
    lateinit var appLinkHelper: AppLinkHelper

    @InjectPresenter
    lateinit var presenter: ReleaseInfoPresenter

    @ProvidePresenter
    fun provideReleasePresenter(): ReleaseInfoPresenter =
        getDependency(ReleaseInfoPresenter::class.java, screenScope)

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies(screenScope)
        super.onCreate(savedInstanceState)
        Log.e("S_DEF_LOG", "ONCRETE $this")
        Log.e("S_DEF_LOG", "ONCRETE REL $arguments, $savedInstanceState")
        arguments?.also { bundle ->
            presenter.releaseId = bundle.getParcelable(ARG_ID)
            presenter.releaseIdCode = bundle.getParcelable(ARG_ID_CODE)
        }
    }

    override fun getBaseLayout(): Int = R.layout.fragment_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = releaseInfoAdapter
            setHasFixedSize(true)
            disableItemChangeAnimation()
        }
    }

    override fun onBackPressed(): Boolean {
        presenter.onBackPressed()
        return true
    }

    override fun showState(state: ReleaseDetailScreenState) {
        state.data?.let { releaseInfoAdapter.bindState(it, state) }
    }

    override fun playEpisodes(release: Release) {
        release.episodes?.let { episodes ->
            playEpisode(release, episodes.last())
        }
    }

    override fun playContinue(release: Release, startWith: Episode) {
        playEpisode(release, startWith, MyPlayerActivity.PLAY_FLAG_FORCE_CONTINUE)
    }

    private fun <T> getUrlByQuality(
        qualityInfo: QualityInfo<T>,
        quality: PrefferedPlayerQuality
    ): AbsoluteUrl? = when (quality) {
        PrefferedPlayerQuality.SD -> qualityInfo.urlSd
        PrefferedPlayerQuality.HD -> qualityInfo.urlHd
        PrefferedPlayerQuality.FULL_HD -> qualityInfo.urlFullHd
        else -> qualityInfo.urlSd
    }

    override fun showDownloadDialog(url: AbsoluteUrl?) {
        val context = context ?: return
        val titles = arrayOf("Внешний загрузчик", "Системный загрузчик")
        AlertDialog.Builder(context)
            .setItems(titles) { _, which ->
                presenter.submitDownloadEpisodeUrlAnalytics()
                when (which) {
                    0 -> appLinkHelper.openLink(url)
                    1 -> systemDownloadWithPermissionCheck(url)
                }
            }
            .show()
    }

    override fun showFileDonateDialog(url: AbsoluteUrl?) {
        val dialogView = LayoutInflater.from(requireView().context)
            .inflate(R.layout.dialog_file_download, null, false)
            .apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

        ImageLoader.getInstance()
            .displayImage("assets://libria_tyan_type3.png", dialogView.dialogFileImage)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .show()

        dialogView.dialogFilePatreonBtn.setOnClickListener {
            presenter.onDialogPatreonClick()
            dialog.dismiss()
        }
        dialogView.dialogFileDonateBtn.setOnClickListener {
            presenter.onDialogDonateClick()
            dialog.dismiss()
        }
        dialogView.dialogFileDownloadBtn.setOnClickListener {
            showDownloadDialog(url)
            dialog.dismiss()
        }
    }

    override fun showEpisodesMenuDialog() {
        val context = context ?: return
        val items = arrayOf(
            "Сбросить историю просмотров",
            "Отметить все как просмотренные"
        )
        AlertDialog.Builder(context)
            .setItems(items) { _, which ->
                when (which) {
                    0 -> presenter.onResetEpisodesHistoryClick()
                    1 -> presenter.onCheckAllEpisodesHistoryClick()
                }
            }
            .show()
    }

    override fun showLongPressEpisodeDialog(episode: Episode) {
        val context = context ?: return
        val items = arrayOf(
            "Отметить как непросмотренная"
        )
        AlertDialog.Builder(context)
            .setItems(items) { _, which ->
                when (which) {
                    0 -> presenter.markEpisodeUnviewed(episode)
                }
            }
            .show()
    }

    override fun downloadEpisode(episode: Episode, quality: PrefferedPlayerQuality?) {
        val qualityInfo = QualityInfo(episode, episode.urlSd, episode.urlHd, episode.urlFullHd)
        if (quality == null) {
            selectQuality(qualityInfo, { selected ->
                presenter.onDownloadLinkSelected(getUrlByQuality(qualityInfo, selected))
            }, true)
        } else {
            presenter.onDownloadLinkSelected(getUrlByQuality(qualityInfo, quality))
        }
    }

    override fun playEpisode(
        release: Release,
        episode: Episode,
        playFlag: Int?,
        quality: PrefferedPlayerQuality?
    ) {
        val qualityInfo = QualityInfo(episode, episode.urlSd, episode.urlHd, episode.urlFullHd)
        selectPlayer({ playerType ->
            if (quality == null) {
                when (playerType) {
                    PrefferedPlayerType.EXTERNAL -> {
                        selectQuality(qualityInfo, { selected ->
                            playExternal(release, episode, selected)
                        }, true)
                    }
                    PrefferedPlayerType.INTERNAL -> {
                        selectQuality(qualityInfo, { selected ->
                            playInternal(release, episode, selected, playFlag)
                        })
                    }
                }
            } else {
                when (playerType) {
                    PrefferedPlayerType.EXTERNAL -> playExternal(
                        release,
                        episode,
                        quality
                    )
                    PrefferedPlayerType.INTERNAL -> playInternal(
                        release,
                        episode,
                        quality,
                        playFlag
                    )
                }
            }
        })
    }

    private fun selectPlayer(
        onSelect: (playerType: PrefferedPlayerType) -> Unit,
        forceDialog: Boolean = false
    ) {
        if (forceDialog) {
            showSelectPlayerDialog(onSelect)
        } else {
            val savedPlayerType = presenter.getPlayerType()
            when (savedPlayerType) {
                PrefferedPlayerType.NOT_SELECTED -> {
                    showSelectPlayerDialog(onSelect)
                }
                PrefferedPlayerType.ALWAYS_ASK -> {
                    showSelectPlayerDialog(onSelect, false)
                }
                PrefferedPlayerType.INTERNAL -> {
                    onSelect(PrefferedPlayerType.INTERNAL)
                }
                PrefferedPlayerType.EXTERNAL -> {
                    onSelect(PrefferedPlayerType.EXTERNAL)
                }
            }
        }
    }

    private fun showSelectPlayerDialog(
        onSelect: (playerType: PrefferedPlayerType) -> Unit,
        savePlayerType: Boolean = true
    ) {
        val titles = arrayOf("Внешний плеер", "Внутренний плеер")
        val context = context ?: return
        AlertDialog.Builder(context)
            .setItems(titles) { dialog, which ->
                val playerType = when (which) {
                    0 -> PrefferedPlayerType.EXTERNAL
                    1 -> PrefferedPlayerType.INTERNAL
                    else -> null
                }
                if (playerType != null) {
                    if (savePlayerType) {
                        presenter.setPlayerType(playerType)
                    }
                    onSelect.invoke(playerType)
                }
            }
            .show()
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun systemDownload(absoluteUrl: AbsoluteUrl?) {
        val url = absoluteUrl?.value ?: return
        val context = context ?: return
        var fileName = systemUtils.getFileNameFromUrl(url)
        val matcher = Pattern.compile("\\?download=([\\s\\S]+)").matcher(fileName)
        if (matcher.find()) {
            fileName = matcher.group(1)
        }
        systemUtils.systemDownloader(url, fileName)
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

    private fun playInternal(
        release: Release,
        episode: Episode,
        quality: PrefferedPlayerQuality,
        playFlag: Int? = null
    ) {
        presenter.submitPlayerOpenAnalytics(
            PrefferedPlayerType.INTERNAL.toAnalyticsPlayer(),
            quality.toAnalyticsQuality()
        )
        startActivity(Intent(context, MyPlayerActivity::class.java).apply {
            putExtra(MyPlayerActivity.ARG_EPISODE_ID, episode.id)
            putExtra(MyPlayerActivity.ARG_QUALITY, quality)
            playFlag?.let {
                putExtra(MyPlayerActivity.ARG_PLAY_FLAG, it)
            }
        })
    }

    private fun playExternal(
        release: Release,
        episode: Episode,
        quality: PrefferedPlayerQuality
    ) {
        presenter.submitPlayerOpenAnalytics(
            PrefferedPlayerType.EXTERNAL.toAnalyticsPlayer(),
            quality.toAnalyticsQuality()
        )
        presenter.markEpisodeViewed(episode)
        val url = when (quality) {
            PrefferedPlayerQuality.SD -> episode.urlSd
            PrefferedPlayerQuality.HD -> episode.urlHd
            PrefferedPlayerQuality.FULL_HD -> episode.urlFullHd
            else -> episode.urlSd
        }
        val fileUri = Uri.parse(url?.value)
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(fileUri, URLConnection.guessContentTypeFromName(fileUri.toString()))
        try {
            startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(context, "Ничего не найдено", Toast.LENGTH_SHORT).show()
        }
    }

    override fun playWeb(link: AbsoluteUrl, releaseLink: RelativeUrl) {
        presenter.onWebPlayerClick()
        startActivity(Intent(context, WebPlayerActivity::class.java).apply {
            putExtra(WebPlayerActivity.ARG_URL, link)
            putExtra(WebPlayerActivity.ARG_RELEASE_LINK, releaseLink)
        })
    }

    private fun <T> selectQuality(
        qualityInfo: QualityInfo<T>,
        onSelect: (quality: PrefferedPlayerQuality) -> Unit,
        forceDialog: Boolean = false
    ) {
        val savedQuality = presenter.getQuality()

        var correctQuality = savedQuality
        if (correctQuality == PrefferedPlayerQuality.FULL_HD && qualityInfo.hasFullHd) {
            correctQuality = PrefferedPlayerQuality.HD
        }
        if (correctQuality == PrefferedPlayerQuality.HD && qualityInfo.hasHd) {
            correctQuality = PrefferedPlayerQuality.SD
        }
        if (correctQuality == PrefferedPlayerQuality.SD && qualityInfo.hasSd) {
            correctQuality = PrefferedPlayerQuality.NOT_SELECTED
        }

        when {
            correctQuality != savedQuality -> showQualityDialog(qualityInfo, onSelect, false)
            forceDialog -> showQualityDialog(qualityInfo, onSelect, false)
            else -> when (savedQuality) {
                PrefferedPlayerQuality.NOT_SELECTED -> showQualityDialog(qualityInfo, onSelect)
                PrefferedPlayerQuality.ALWAYS_ASK -> showQualityDialog(qualityInfo, onSelect, false)
                else -> onSelect(savedQuality)
            }
        }
    }

    private fun <T> showQualityDialog(
        qualityInfo: QualityInfo<T>,
        onSelect: (quality: PrefferedPlayerQuality) -> Unit,
        saveQuality: Boolean = true
    ) {
        val context = context ?: return

        val qualities = mutableListOf<PrefferedPlayerQuality>()
        if (qualityInfo.hasSd) qualities.add(PrefferedPlayerQuality.SD)
        if (qualityInfo.hasHd) qualities.add(PrefferedPlayerQuality.HD)
        if (qualityInfo.hasFullHd) qualities.add(PrefferedPlayerQuality.FULL_HD)

        val titles = qualities
            .map {
                when (it) {
                    PrefferedPlayerQuality.SD -> "480p"
                    PrefferedPlayerQuality.HD -> "720p"
                    PrefferedPlayerQuality.FULL_HD -> "1080p"
                    else -> "Unknown"
                }
            }
            .toTypedArray()

        AlertDialog.Builder(context)
            .setTitle("Качество")
            .setItems(titles) { _, p1 ->
                val quality = qualities.getOrNull(p1)
                if (quality != null) {
                    if (saveQuality) {
                        presenter.setQuality(quality)
                    }
                    onSelect.invoke(quality)
                }
            }
            .show()
    }

    override fun showFavoriteDialog() {
        val context = context ?: return
        AlertDialog.Builder(context)
            .setMessage("Для выполнения действия необходимо авторизоваться. Авторизоваться?")
            .setPositiveButton("Да") { _, _ -> presenter.openAuth() }
            .setNegativeButton("Нет", null)
            .show()
    }

    private val headListener = object : ReleaseHeadDelegate.Listener {

        override fun onClickSomeLink(url: String) {
            presenter.onClickLink(url)
        }

        override fun onClickTag(text: String) {
            presenter.openSearch(text)
        }

        override fun onClickFav() {
            presenter.onClickFav()
        }

        override fun onScheduleClick(day: DayOfWeek) {
            presenter.onScheduleClick(day)
        }

        override fun onExpandClick() {
            presenter.onDescriptionExpandClick()
        }
    }

    private val episodeListener = object : ReleaseEpisodeDelegate.Listener {

        override fun onClickSd(episode: ReleaseEpisodeItemState) {
            presenter.onEpisodeClick(
                episode,
                MyPlayerActivity.PLAY_FLAG_FORCE_CONTINUE,
                PrefferedPlayerQuality.SD
            )
        }

        override fun onClickHd(episode: ReleaseEpisodeItemState) {
            presenter.onEpisodeClick(
                episode,
                MyPlayerActivity.PLAY_FLAG_FORCE_CONTINUE,
                PrefferedPlayerQuality.HD
            )
        }

        override fun onClickFullHd(episode: ReleaseEpisodeItemState) {
            presenter.onEpisodeClick(
                episode,
                MyPlayerActivity.PLAY_FLAG_FORCE_CONTINUE,
                PrefferedPlayerQuality.FULL_HD
            )
        }

        override fun onClickEpisode(episode: ReleaseEpisodeItemState) {
            presenter.onEpisodeClick(episode, MyPlayerActivity.PLAY_FLAG_FORCE_CONTINUE)
        }

        override fun onLongClickEpisode(episode: ReleaseEpisodeItemState) {
            presenter.onLongClickEpisode(episode)
        }
    }

    private val episodeControlListener = object : ReleaseEpisodeControlDelegate.Listener {

        override fun onClickWatchWeb(place: EpisodeControlPlace) {
            presenter.onClickWatchWeb(place)
        }

        override fun onClickWatchAll(place: EpisodeControlPlace) {
            presenter.onPlayAllClick(place)
        }

        override fun onClickContinue(place: EpisodeControlPlace) {
            presenter.onClickContinue(place)
        }

        override fun onClickEpisodesMenu(place: EpisodeControlPlace) {
            presenter.onClickEpisodesMenu(place)
        }
    }

    data class QualityInfo<T>(
        val data: T,
        val urlSd: AbsoluteUrl?,
        val urlHd: AbsoluteUrl?,
        val urlFullHd: AbsoluteUrl?
    ) {
        val hasSd = urlSd != null
        val hasHd = urlHd != null
        val hasFullHd = urlFullHd != null
    }

}