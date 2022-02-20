package ru.radiationx.anilibria.screen.update.source

import android.os.Bundle
import android.view.View
import androidx.leanback.widget.GuidedAction
import ru.radiationx.anilibria.R
import ru.radiationx.anilibria.common.fragment.scoped.ScopedGuidedStepFragment
import ru.radiationx.shared.ktx.android.subscribeTo
import ru.radiationx.shared_app.di.viewModel
import tv.anilibria.feature.appupdates.data.domain.UpdateLink

class UpdateSourceGuidedFragment : ScopedGuidedStepFragment() {

    private val viewModel by viewModel<UpdateSourceViewModel>()

    override fun onProvideTheme(): Int = R.style.AppTheme_Player_LeanbackWizard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeTo(viewModel.sourcesData) {
            actions = it.mapIndexed { index: Int, updateLink: UpdateLink ->
                GuidedAction.Builder(requireContext())
                    .id(index.toLong())
                    .title(updateLink.name)
                    .build()
            }
        }
    }

    override fun onGuidedActionClicked(action: GuidedAction) {
        viewModel.onLinkClick(action.id.toInt())
    }
}