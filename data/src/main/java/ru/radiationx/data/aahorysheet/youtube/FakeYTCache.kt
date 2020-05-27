package ru.radiationx.data.aahorysheet.youtube

import ru.radiationx.data.aahorysheet.ReadableFake
import ru.radiationx.data.aahorysheet.WritableFake
import ru.radiationx.data.adomain.youtube.Youtube

interface FakeYTCache : ReadableFake<Youtube>, WritableFake<Youtube>