package ru.radiationx.data.aahorysheet.release

import ru.radiationx.data.aahorysheet.ReadableFake
import ru.radiationx.data.aahorysheet.WritableFake
import ru.radiationx.data.adomain.release.Release

interface FakeReleaseDb : ReadableFake<Release>, WritableFake<Release>