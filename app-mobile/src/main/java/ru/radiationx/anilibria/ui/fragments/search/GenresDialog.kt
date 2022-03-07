package ru.radiationx.anilibria.ui.fragments.search

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.RadioGroup
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.dialog_genres.view.*
import ru.radiationx.anilibria.R
import ru.radiationx.anilibria.extension.fillNavigationBarColor
import ru.radiationx.anilibria.extension.getColorFromAttr
import ru.radiationx.shared.ktx.android.visible
import tv.anilibria.feature.domain.entity.ReleaseGenre
import tv.anilibria.feature.domain.entity.ReleaseSeason
import tv.anilibria.feature.domain.entity.ReleaseYear
import tv.anilibria.feature.domain.entity.SearchForm


class GenresDialog(
    private val context: Context,
    private val listener: ClickListener
) {
    private val dialog: BottomSheetDialog = BottomSheetDialog(context)
    private var rootView: View =
        LayoutInflater.from(context).inflate(R.layout.dialog_genres, null, false)

    private val filterComplete = rootView.filterComplete

    private val sortingGroup = rootView.sortingRadioGroup
    private val sortingPopular = rootView.sortingPopular
    private val sortingNew = rootView.sortingNew

    private val genresChipGroup = rootView.genresChips
    private val genresChips = mutableListOf<Chip>()

    private val yearsChipGroup = rootView.yearsChips
    private val yearsChips = mutableListOf<Chip>()

    private val seasonsChipGroup = rootView.seasonsChips
    private val seasonsChips = mutableListOf<Chip>()

    private val genreItems = mutableListOf<ReleaseGenre>()
    private val yearItems = mutableListOf<ReleaseYear>()
    private val seasonItems = mutableListOf<ReleaseSeason>()

    private val checkedGenres = mutableSetOf<ReleaseGenre>()
    private val checkedYears = mutableSetOf<ReleaseYear>()
    private val checkedSeasons = mutableSetOf<ReleaseSeason>()

    private var currentSorting = SearchForm.Sort.DATE
    private var currentComplete = false

    private var actionButton: View
    private var actionButtonText: TextView
    private var actionButtonCount: TextView


    private val genresChipListener =
        CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkedGenres.add(genreItems.first { it.value.hashCode() == buttonView.id })
            } else {
                checkedGenres.remove(genreItems.first { it.value.hashCode() == buttonView.id })
            }
            listener.onCheckedGenres(checkedGenres.toList())
        }

    private val yearsChipListener =
        CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkedYears.add(yearItems.first { it.value.hashCode() == buttonView.id })
            } else {
                checkedYears.remove(yearItems.first { it.value.hashCode() == buttonView.id })
            }
            listener.onCheckedYears(checkedYears.toList())
        }

    private val seasonsChipListener =
        CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkedSeasons.add(seasonItems.first { it.value.hashCode() == buttonView.id })
            } else {
                checkedSeasons.remove(seasonItems.first { it.value.hashCode() == buttonView.id })
            }
            listener.onCheckedSeasons(checkedSeasons.toList())
        }

    private val sortingListener = RadioGroup.OnCheckedChangeListener { _, _ ->
        currentSorting = when {
            sortingPopular.isChecked -> SearchForm.Sort.RATING
            sortingNew.isChecked -> SearchForm.Sort.DATE
            else -> currentSorting
        }
        listener.onChangeSorting(currentSorting)
    }

    private val completeListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
        currentComplete = isChecked
        listener.onChangeComplete(currentComplete)
    }

    init {
        dialog.setContentView(rootView)
        val parentView = rootView.parent as FrameLayout
        val coordinatorLayout = parentView.parent as CoordinatorLayout
        val bottomSheetView =
            coordinatorLayout.findViewById<ViewGroup>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheetView.apply {
            setPadding(
                paddingLeft,
                paddingTop,
                paddingRight,
                (resources.displayMetrics.density * 40).toInt()
            )
        }

        actionButton = LayoutInflater.from(context)
            .inflate(R.layout.picker_bottom_action_button, coordinatorLayout, false)
        actionButtonText = actionButton.findViewById(R.id.pickerActionText)
        actionButtonCount = actionButton.findViewById(R.id.pickerActionCounter)

        coordinatorLayout.addView(
            actionButton,
            (actionButton.layoutParams as CoordinatorLayout.LayoutParams).also {
                it.gravity = Gravity.BOTTOM
            })
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            actionButton.z = parentView.z
        }

        sortingGroup.setOnCheckedChangeListener(sortingListener)
        filterComplete.setOnCheckedChangeListener(completeListener)

        actionButton.setOnClickListener {
            dialog.dismiss()
            listener.onAccept()
        }

        dialog.setOnDismissListener {
            listener.onClose()
        }
    }

    fun setItems(newItems: List<ReleaseGenre>) {
        genreItems.clear()
        genreItems.addAll(newItems)

        updateGenreViews()
        updateChecked()
    }

    fun setYears(newItems: List<ReleaseYear>) {
        yearItems.clear()
        yearItems.addAll(newItems)

        updateYearViews()
        updateChecked()
    }

    fun setSeasons(newItems: List<ReleaseSeason>) {
        seasonItems.clear()
        seasonItems.addAll(newItems)

        updateSeasonViews()
        updateChecked()
    }

    private fun updateGenreViews() {
        genresChipGroup.removeAllViews()
        genresChips.clear()
        genreItems.forEach { genre ->
            val chip = Chip(genresChipGroup.context).also {
                Log.e("lululu", "set id=${genre.value.hashCode()} to '${genre.value}'")
                it.id = genre.value.hashCode()
                it.text = genre.value.capitalize()
                it.isCheckable = true
                it.isClickable = true
                it.isChecked = checkedGenres.contains(genre)
                it.setTextColor(it.context.getColorFromAttr(R.attr.textDefault))
                it.setChipBackgroundColorResource(R.color.bg_chip)
                it.setOnCheckedChangeListener(genresChipListener)
            }
            genresChipGroup.addView(chip)
            genresChips.add(chip)
        }
    }

    private fun updateYearViews() {
        yearsChipGroup.removeAllViews()
        yearsChips.clear()
        yearItems.forEach { year ->
            val chip = Chip(yearsChipGroup.context).also {
                Log.e("lululu", "set id=${year.value.hashCode()} to '${year.value}'")
                it.id = year.value.hashCode()
                it.text = year.value.capitalize()
                it.isCheckable = true
                it.isClickable = true
                it.isChecked = checkedYears.contains(year)
                it.setTextColor(it.context.getColorFromAttr(R.attr.textDefault))
                it.setChipBackgroundColorResource(R.color.bg_chip)
                it.setOnCheckedChangeListener(yearsChipListener)
            }
            yearsChipGroup.addView(chip)
            yearsChips.add(chip)
        }
    }

    private fun updateSeasonViews() {
        seasonsChipGroup.removeAllViews()
        seasonsChips.clear()
        seasonItems.forEach { season ->
            val chip = Chip(seasonsChipGroup.context).also {
                Log.e("lululu", "set id=${season.value.hashCode()} to '${season.value}'")
                it.id = season.value.hashCode()
                it.text = season.value.capitalize()
                it.isCheckable = true
                it.isClickable = true
                it.isChecked = checkedSeasons.contains(season)
                it.setTextColor(it.context.getColorFromAttr(R.attr.textDefault))
                it.setChipBackgroundColorResource(R.color.bg_chip)
                it.setOnCheckedChangeListener(seasonsChipListener)
            }
            seasonsChipGroup.addView(chip)
            seasonsChips.add(chip)
        }
    }

    private fun updateChecked() {
        genresChips.forEach { chip ->
            chip.isChecked = checkedGenres.any { it.hashCode() == chip.id }
        }
        yearsChips.forEach { chip ->
            chip.isChecked = checkedYears.any { it.hashCode() == chip.id }
        }
        seasonsChips.forEach { chip ->
            chip.isChecked = checkedSeasons.any { it.hashCode() == chip.id }
        }
        val allCount = checkedGenres.size + checkedYears.size + checkedSeasons.size
        actionButtonCount.text = "$allCount"
        actionButtonCount.visible(allCount > 0)
    }

    fun setCheckedGenres(items: List<ReleaseGenre>) {
        checkedGenres.clear()
        checkedGenres.addAll(items)
        updateChecked()
    }

    fun setCheckedYears(items: List<ReleaseYear>) {
        checkedYears.clear()
        checkedYears.addAll(items)
        updateChecked()
    }

    fun setCheckedSeasons(items: List<ReleaseSeason>) {
        checkedSeasons.clear()
        checkedSeasons.addAll(items)
        updateChecked()
    }

    fun setSorting(sorting: SearchForm.Sort) {
        currentSorting = sorting
        sortingGroup.setOnCheckedChangeListener(null)
        when (currentSorting) {
            SearchForm.Sort.RATING -> sortingPopular.isChecked = true
            SearchForm.Sort.DATE -> sortingNew.isChecked = true
        }
        sortingGroup.setOnCheckedChangeListener(sortingListener)
    }

    fun setComplete(complete: Boolean) {
        currentComplete = complete
        filterComplete.setOnCheckedChangeListener(null)
        filterComplete.isChecked = currentComplete
        filterComplete.setOnCheckedChangeListener(completeListener)
    }

    fun showDialog() {
        updateGenreViews()
        dialog.fillNavigationBarColor()
        dialog.show()
        //expandDialog()
    }

    private fun expandDialog() {
        getBehavior()?.also {
            it.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun getBehavior(): BottomSheetBehavior<View>? {
        val bottomSheetInternal =
            dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        return BottomSheetBehavior.from(bottomSheetInternal!!)
    }

    interface ClickListener {
        fun onAccept()
        fun onClose()
        fun onCheckedGenres(items: List<ReleaseGenre>)
        fun onCheckedYears(items: List<ReleaseYear>)
        fun onCheckedSeasons(items: List<ReleaseSeason>)
        fun onChangeSorting(sorting: SearchForm.Sort)
        fun onChangeComplete(complete: Boolean)
    }
}
