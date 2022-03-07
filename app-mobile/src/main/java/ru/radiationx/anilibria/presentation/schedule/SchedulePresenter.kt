package ru.radiationx.anilibria.presentation.schedule

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayAt
import moxy.InjectViewState
import ru.radiationx.anilibria.model.ScheduleItemState
import ru.radiationx.anilibria.model.toScheduleState
import ru.radiationx.anilibria.navigation.Screens
import ru.radiationx.anilibria.presentation.common.BasePresenter
import ru.radiationx.anilibria.presentation.common.IErrorHandler
import ru.radiationx.anilibria.ui.fragments.schedule.ScheduleDayState
import ru.radiationx.anilibria.ui.fragments.schedule.ScheduleScreenState
import ru.radiationx.shared.ktx.asDayName
import ru.terrakok.cicerone.Router
import tv.anilibria.feature.content.data.BaseUrlHelper
import tv.anilibria.feature.content.data.analytics.AnalyticsConstants
import tv.anilibria.feature.content.data.analytics.features.ReleaseAnalytics
import tv.anilibria.feature.content.data.analytics.features.ScheduleAnalytics
import tv.anilibria.feature.content.data.repos.ScheduleRepository
import tv.anilibria.feature.domain.entity.schedule.ScheduleDay
import javax.inject.Inject

@InjectViewState
class SchedulePresenter @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val router: Router,
    private val errorHandler: IErrorHandler,
    private val scheduleAnalytics: ScheduleAnalytics,
    private val releaseAnalytics: ReleaseAnalytics,
    private val urlHelper: BaseUrlHelper
) : BasePresenter<ScheduleView>(router) {

    private var firstData = true
    var argDay: DayOfWeek? = null

    private var currentState = ScheduleScreenState()
    private val currentDays = mutableListOf<ScheduleDay>()

    private fun updateState(block: (ScheduleScreenState) -> ScheduleScreenState) {
        currentState = block.invoke(currentState)
        viewState.showState(currentState)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        scheduleRepository
            .observeSchedule()
            .onEach { scheduleDays ->
                currentDays.clear()
                currentDays.addAll(scheduleDays)
                val dayStates = scheduleDays.map { scheduleDay ->

                    val currentDate = Clock.System.todayAt(TimeZone.currentSystemDefault())
                    val calendarDay = currentDate.dayOfWeek

                    var dayName = scheduleDay.day.asDayName()
                    if (scheduleDay.day == calendarDay) {
                        dayName += " (сегодня)"
                    }
                    val items = scheduleDay.items.map { it.toScheduleState(urlHelper) }
                    ScheduleDayState(dayName, items)
                }

                updateState {
                    it.copy(dayItems = dayStates)
                }
                handleFirstData()
            }
            .launchIn(viewModelScope)
    }

    private fun handleFirstData() {
        if (firstData) {
            firstData = false
            val currentDay = if (argDay != null) {
                argDay
            } else {
                Clock.System.todayAt(TimeZone.currentSystemDefault()).dayOfWeek
            }
            currentDays
                .indexOfFirst { it.day == currentDay }
                .let { currentState.dayItems.getOrNull(it) }
                ?.also { viewState.scrollToDay(it) }
        }
    }

    fun onHorizontalScroll(position: Int) {
        scheduleAnalytics.horizontalScroll(position)
    }

    fun onItemClick(item: ScheduleItemState, position: Int) {
        val releaseItem = currentDays
            .flatMap { it.items }
            .find { it.id == item.releaseId }
            ?: return
        scheduleAnalytics.releaseClick(position)
        releaseAnalytics.open(AnalyticsConstants.screen_schedule, releaseItem.id.id)
        router.navigateTo(Screens.ReleaseDetails(releaseItem.id))
    }

    fun refresh() {
        viewModelScope.launch {
            updateState {
                it.copy(refreshing = true)
            }
            runCatching {
                scheduleRepository.loadSchedule()
            }.onSuccess {
                updateState {
                    it.copy(refreshing = false)
                }
            }.onFailure {
                updateState {
                    it.copy(refreshing = false)
                }
                errorHandler.handle(it)
            }
        }
    }
}