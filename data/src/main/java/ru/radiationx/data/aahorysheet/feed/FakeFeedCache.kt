package ru.radiationx.data.aahorysheet.feed

import ru.radiationx.data.aahorysheet.ReadableFake
import ru.radiationx.data.aahorysheet.WritableFake
import ru.radiationx.data.adomain.feed.FeedItem
import ru.radiationx.data.adomain.release.Release
import ru.radiationx.data.adomain.youtube.YouTube

interface FakeFeedCache : ReadableFake<FeedRelative>, WritableFake<FeedRelative>