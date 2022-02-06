package tv.anilibria.module.data.network.entity.app.release

data class BlockedInfoResponse(
    val isBlocked: Boolean,
    val reason: String?
)