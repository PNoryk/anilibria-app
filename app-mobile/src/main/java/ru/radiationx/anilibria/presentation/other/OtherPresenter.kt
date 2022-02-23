package ru.radiationx.anilibria.presentation.other

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.radiationx.shared_app.AppLinkHelper
import ru.radiationx.anilibria.R
import ru.radiationx.anilibria.model.asDataIconRes
import ru.radiationx.anilibria.model.loading.StateController
import ru.radiationx.anilibria.model.toState
import ru.radiationx.anilibria.navigation.Screens
import ru.radiationx.anilibria.presentation.common.BasePresenter
import ru.radiationx.anilibria.presentation.common.IErrorHandler
import ru.radiationx.anilibria.ui.fragments.other.OtherMenuItemState
import ru.radiationx.anilibria.ui.fragments.other.ProfileScreenState
import ru.radiationx.anilibria.utils.messages.SystemMessenger
import ru.terrakok.cicerone.Router
import tv.anilibria.core.types.RelativeUrl
import tv.anilibria.module.data.AuthStateHolder
import tv.anilibria.module.data.BaseUrlHelper
import tv.anilibria.module.data.analytics.AnalyticsConstants
import tv.anilibria.module.data.analytics.features.*
import tv.anilibria.module.data.repos.AuthRepository
import tv.anilibria.module.data.repos.MenuRepository
import tv.anilibria.module.data.repos.UserRepository
import tv.anilibria.module.domain.entity.AuthState
import tv.anilibria.module.domain.entity.other.LinkMenuItem
import tv.anilibria.module.domain.entity.other.User
import javax.inject.Inject

@InjectViewState
class OtherPresenter @Inject constructor(
    private val router: Router,
    private val systemMessenger: SystemMessenger,
    private val authStateHolder: AuthStateHolder,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val errorHandler: IErrorHandler,
    private val menuRepository: MenuRepository,
    private val authDeviceAnalytics: AuthDeviceAnalytics,
    private val authMainAnalytics: AuthMainAnalytics,
    private val historyAnalytics: HistoryAnalytics,
    private val otherAnalytics: OtherAnalytics,
    private val settingsAnalytics: SettingsAnalytics,
    private val pageAnalytics: PageAnalytics,
    private val donationDetailAnalytics: DonationDetailAnalytics,
    private val urlHelper: BaseUrlHelper,
    private val appLinkHelper: AppLinkHelper
) : BasePresenter<OtherView>(router) {

    companion object {
        const val MENU_HISTORY = 0
        const val MENU_TEAM = 1
        const val MENU_DONATE = 2
        const val MENU_SETTINGS = 3
        const val MENU_OTP_CODE = 4

        val PAGE_PATH_TEAM = RelativeUrl("pages/team.php")
        val PAGE_PATH_DONATE = RelativeUrl("pages/donate.php")
    }

    private val stateController = StateController(ProfileScreenState())

    private var currentProfileItem: User? = null
    private val currentLinkMenuItems = mutableListOf<LinkMenuItem>()
    private val linksMap = mutableMapOf<Int, LinkMenuItem>()

    private val allMainMenu = mutableListOf<OtherMenuItemState>()
    private val allSystemMenu = mutableListOf<OtherMenuItemState>()
    private val allLinkMenu = mutableListOf<OtherMenuItemState>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        stateController
            .observeState()
            .onEach { viewState.showState(it) }
            .launchIn(viewModelScope)

        allMainMenu.add(OtherMenuItemState(MENU_HISTORY, "История", R.drawable.ic_history))
        allMainMenu.add(
            OtherMenuItemState(
                MENU_TEAM,
                "Список команды",
                R.drawable.ic_account_multiple
            )
        )
        allMainMenu.add(OtherMenuItemState(MENU_DONATE, "Поддержать", R.drawable.ic_gift))
        allMainMenu.add(
            OtherMenuItemState(
                MENU_OTP_CODE,
                "Привязать устройство",
                R.drawable.ic_devices_other
            )
        )

        allSystemMenu.add(OtherMenuItemState(MENU_SETTINGS, "Настройки", R.drawable.ic_settings))

        viewModelScope.launch {
            runCatching {
                menuRepository.getMenu()
            }.onFailure {
                it.printStackTrace()
            }
        }

        subscribeUpdate()
        updateMenuItems()
    }

    override fun attachView(view: OtherView?) {
        super.attachView(view)
        viewModelScope.launch {
            runCatching { userRepository.loadUser() }
        }
    }

    fun onProfileClick() {
        viewModelScope.launch {
            if (authStateHolder.get() == AuthState.AUTH) {
                otherAnalytics.profileClick()
            } else {
                otherAnalytics.loginClick()
                authMainAnalytics.open(AnalyticsConstants.screen_other)
                router.navigateTo(Screens.Auth())
            }
        }
    }

    fun signOut() {
        otherAnalytics.logoutClick()
        GlobalScope.launch {
            runCatching {
                authRepository.signOut()
            }.onSuccess {
                systemMessenger.showMessage("Данные авторизации удалены")
            }.onFailure {
                errorHandler.handle(it)
            }
        }
    }

    fun onMenuClick(item: OtherMenuItemState) {
        when (item.id) {
            MENU_HISTORY -> {
                otherAnalytics.historyClick()
                historyAnalytics.open(AnalyticsConstants.screen_other)
                router.navigateTo(Screens.History())
            }
            MENU_TEAM -> {
                otherAnalytics.teamClick()
                pageAnalytics.open(AnalyticsConstants.screen_other, PAGE_PATH_TEAM.value)
                router.navigateTo(Screens.StaticPage(PAGE_PATH_TEAM, "Список команды"))
            }
            MENU_DONATE -> {
                otherAnalytics.donateClick()
                donationDetailAnalytics.open(AnalyticsConstants.screen_other)
                router.navigateTo(Screens.DonationDetail())
            }
            MENU_SETTINGS -> {
                otherAnalytics.settingsClick()
                router.navigateTo(Screens.Settings())
            }
            MENU_OTP_CODE -> {
                settingsAnalytics.open(AnalyticsConstants.screen_other)
                otherAnalytics.authDeviceClick()
                authDeviceAnalytics.open(AnalyticsConstants.screen_other)
                viewState.showOtpCode()
            }
            else -> {
                linksMap[item.id]?.also { linkItem ->
                    otherAnalytics.linkClick(linkItem.title)
                    val absoluteLink = linkItem.absoluteLink
                    val pagePath = linkItem.sitePagePath
                    when {
                        absoluteLink != null -> appLinkHelper.openLink(absoluteLink)
                        pagePath != null -> {
                            pageAnalytics.open(AnalyticsConstants.screen_other, pagePath.value)
                            router.navigateTo(Screens.StaticPage(pagePath))
                        }
                    }
                }
            }
        }
    }

    private fun subscribeUpdate() {
        userRepository
            .observeUser()
            .onEach {
                currentProfileItem = it
                updateMenuItems()
            }
            .launchIn(viewModelScope)

        menuRepository
            .observeMenu()
            .onEach { linkItems ->
                currentLinkMenuItems.clear()
                currentLinkMenuItems.addAll(linkItems)
                allLinkMenu.clear()
                allLinkMenu.addAll(linkItems.map {
                    OtherMenuItemState(
                        id = it.hashCode(),
                        title = it.title,
                        iconRes = it.icon?.asDataIconRes() ?: R.drawable.ic_link
                    )
                })
                linksMap.clear()
                linksMap.putAll(linkItems.associateBy { it.hashCode() })
                updateMenuItems()
            }
            .launchIn(viewModelScope)
    }

    private fun updateMenuItems() {
        viewModelScope.launch {
            // Для фильтрации, если вдруг понадобится добавить
            val mainMenu = allMainMenu.toMutableList()
            val systemMenu = allSystemMenu.toMutableList()
            val linkMenu = allLinkMenu.toMutableList()

            if (authStateHolder.get() != AuthState.AUTH) {
                mainMenu.removeAll { it.id == MENU_OTP_CODE }
            }

            val profileState = currentProfileItem?.toState(urlHelper, authStateHolder.get())
            val menuState = listOf(mainMenu, systemMenu, linkMenu).filter { it.isNotEmpty() }
            stateController.updateState {
                it.copy(profile = profileState, menuItems = menuState)
            }
        }
    }
}
