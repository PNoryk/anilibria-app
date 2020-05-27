package ru.radiationx.data.aahorysheet.release

import ru.radiationx.data.aahorysheet.ReadableFake
import ru.radiationx.data.aahorysheet.WritableFake
import ru.radiationx.data.adomain.entity.release.Release

interface FakeReleaseCache : ReadableFake<Release>, WritableFake<Release>