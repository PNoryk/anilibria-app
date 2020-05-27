package ru.radiationx.data.aahorysheet.feed

import ru.radiationx.data.aahorysheet.ReadableFake
import ru.radiationx.data.aahorysheet.WritableFake
import ru.radiationx.data.adomain.feed.FeedItem
import ru.radiationx.data.adomain.youtube.YouTube

interface FakeFeedDb : ReadableFake<FeedRelative>, WritableFake<FeedRelative>