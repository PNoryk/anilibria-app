package ru.radiationx.anilibria.ui.adapters.other

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.nostra13.universalimageloader.core.ImageLoader
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_other_profile.*
import ru.radiationx.anilibria.R
import ru.radiationx.shared_app.di.DI
import ru.radiationx.anilibria.ui.adapters.ListItem
import ru.radiationx.anilibria.ui.adapters.ProfileListItem
import ru.radiationx.anilibria.ui.common.adapters.AppAdapterDelegate
import ru.radiationx.anilibria.utils.DimensionsProvider
import ru.radiationx.data.entity.app.other.ProfileItem
import ru.radiationx.data.entity.common.AuthState
import ru.radiationx.shared.ktx.android.gone
import ru.radiationx.shared.ktx.android.visible

class ProfileItemDelegate(
        private val clickListener: (ProfileItem) -> Unit,
        private val logoutClickListener: () -> Unit
) : AppAdapterDelegate<ProfileListItem, ListItem, ProfileItemDelegate.ViewHolder>(
        R.layout.item_other_profile,
        { it is ProfileListItem },
        { ViewHolder(it, clickListener, logoutClickListener) }
) {

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        (holder as ViewHolder).onDetach()
    }

    override fun bindData(item: ProfileListItem, holder: ViewHolder) = holder.bind(item.profileItem)

    class ViewHolder(
            override val containerView: View,
            private val clickListener: (ProfileItem) -> Unit,
            private val logoutClickListener: () -> Unit
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private val dimensionsProvider = DI.get(DimensionsProvider::class.java)
        private var compositeDisposable = CompositeDisposable()

        private lateinit var item: ProfileItem

        init {
            compositeDisposable.add(dimensionsProvider.observe().subscribe {
                containerView.setPadding(
                        containerView.paddingLeft,
                        it.statusBar,
                        containerView.paddingRight,
                        containerView.paddingBottom
                )
            })
            containerView.setOnClickListener { clickListener(item) }
            profileLogout.setOnClickListener { logoutClickListener() }
        }

        fun bind(profileItem: ProfileItem) {
            item = profileItem
            Log.e("S_DEF_LOG", "bind prfile $profileItem")
            if (profileItem.avatarUrl.isNullOrEmpty()) {
                ImageLoader.getInstance().displayImage("assets://res/alib_new_or_b.png", profileAvatar)
            } else {
                ImageLoader.getInstance().displayImage(profileItem.avatarUrl, profileAvatar)
            }
            profileLogout.visible(profileItem.authState == AuthState.AUTH)
            if (profileItem.authState == AuthState.AUTH) {
                profileNick.text = profileItem.nick
                profileDesc.text = "Перейти в профиль"
                profileDesc.gone()
            } else {
                profileNick.text = "Гость"
                profileDesc.text = "Авторизоваться"
                profileDesc.visible()
            }
        }

        fun onDetach() {
            compositeDisposable.clear()
        }
    }
}
