package ru.radiationx.data.aahorysheet.feed

import ru.radiationx.data.aahorysheet.ReadableFake
import ru.radiationx.data.aahorysheet.WritableFake

interface FakeFeedDb : ReadableFake<FeedRelative>, WritableFake<FeedRelative>