package ru.radiationx.anilibria.screen.main

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayAt
import ru.radiationx.anilibria.common.BaseCardsViewModel
import ru.radiationx.anilibria.common.CardsDataConverter
import ru.radiationx.anilibria.common.LibriaCard
import ru.radiationx.anilibria.common.LinkCard
import ru.radiationx.anilibria.screen.DetailsScreen
import ru.radiationx.anilibria.screen.ScheduleScreen
import ru.radiationx.shared.ktx.asDayNameDeclension
import ru.radiationx.shared.ktx.asDayPretext
import ru.terrakok.cicerone.Router
import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.ReleaseInteractor
import tv.anilibria.feature.content.data.repos.ScheduleRepository
import tv.anilibria.feature.content.types.release.ReleaseId

@InjectConstructor
class MainScheduleViewModel(
    private val scheduleRepository: ScheduleRepository,
    private val releaseInteractor: ReleaseInteractor,
    private val converter: CardsDataConverter,
    private val router: Router
) : BaseCardsViewModel() {

    override val defaultTitle: String = "Ожидается сегодня"

    override val loadMoreCard: LinkCard =
        LinkCard("Открыть полное расписание")

    override val loadOnCreate: Boolean = false

    override fun onColdCreate() {
        super.onColdCreate()
        onRefreshClick()
    }

    override suspend fun getCoLoader(requestPage: Int): List<LibriaCard> = scheduleRepository
        .loadSchedule()
        .let { schedueDays ->
            val mskDay = Clock.System.todayAt(TimeZone.of("MSK")).dayOfWeek
            val currentDay = Clock.System.todayAt(TimeZone.currentSystemDefault()).dayOfWeek

            val dayTitle = if (currentDay == mskDay) {
                "Ожидается сегодня"
            } else {
                val preText = mskDay.asDayPretext()
                val dayName = mskDay.asDayNameDeclension().toLowerCase()
                "Ожидается $preText $dayName (по МСК)"
            }

            rowTitle.value = dayTitle

            val items = schedueDays.firstOrNull { it.day == mskDay }?.items.orEmpty()

            items.map { converter.toCard(it) }
        }

    override fun hasMoreCards(newCards: List<LibriaCard>, allCards: List<LibriaCard>): Boolean {
        return true
    }

    override fun onLinkCardClick() {
        router.navigateTo(ScheduleScreen())
    }

    override fun onLibriaCardClick(card: LibriaCard) {
        router.navigateTo(DetailsScreen(ReleaseId(card.id)))
    }
}