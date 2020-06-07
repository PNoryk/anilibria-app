package anilibria.tv.cache.impl.memory

import anilibria.tv.cache.impl.common.amazing.AmazingMemoryDataSource
import anilibria.tv.cache.impl.memory.keys.ReleaseMemoryKey
import anilibria.tv.domain.entity.release.Release
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import toothpick.InjectConstructor
import java.util.*

@InjectConstructor
class ReleaseMemoryDataSource : AmazingMemoryDataSource<ReleaseMemoryKey, Release>()