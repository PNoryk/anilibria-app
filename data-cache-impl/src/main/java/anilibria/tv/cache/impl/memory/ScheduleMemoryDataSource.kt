package anilibria.tv.cache.impl.memory

import anilibria.tv.cache.impl.common.amazing.AmazingMemoryDataSource
import anilibria.tv.cache.impl.memory.keys.ScheduleMemoryKey
import anilibria.tv.domain.entity.relative.ScheduleDayRelative
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import toothpick.InjectConstructor
import java.util.*

@InjectConstructor
class ScheduleMemoryDataSource : AmazingMemoryDataSource<ScheduleMemoryKey, ScheduleDayRelative>()