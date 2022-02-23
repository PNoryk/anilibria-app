package ru.radiationx.anilibria.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener
import ru.radiationx.anilibria.App
import ru.radiationx.anilibria.ui.activities.main.IntentActivity
import ru.radiationx.shared.ktx.android.centerCrop
import ru.radiationx.shared.ktx.android.createAvatar
import tv.anilibria.module.data.BaseUrlHelper
import tv.anilibria.module.domain.entity.release.Release
import kotlin.math.min

class ShortcutHelper(
    private val urlHelper: BaseUrlHelper
) {

    fun addShortcut(data: Release) {
        val posterUrl = urlHelper.makeMedia(data.poster)?.value
        ImageLoader.getInstance()
            .loadImage(posterUrl, object : SimpleImageLoadingListener() {
                override fun onLoadingComplete(
                    imageUri: String?,
                    view: View?,
                    loadedImage: Bitmap
                ) {
                    val minSize = min(loadedImage.width, loadedImage.height)
                    val bmp = loadedImage.centerCrop(minSize, minSize).createAvatar(isCircle = true)
                    addShortcut(data, bmp)
                }
            })
    }

    fun addShortcut(data: Release, bitmap: Bitmap) = addShortcut(
        context = App.instance,
        id = "release_${data.id}",
        shortLabel = data.nameRus?.text.toString(),
        longLabel = data.names?.joinToString(" / ") { it.text }.toString(),
        url = urlHelper.makeSite(data.link)?.value.orEmpty(),
        bitmap = bitmap
    )

    private fun addShortcut(
        context: Context,
        id: String,
        shortLabel: String,
        longLabel: String,
        url: String,
        bitmap: Bitmap
    ) {
        Log.e("lalala", "addShortcut $id, $shortLabel, $longLabel, $url")
        val shortcut = ShortcutInfoCompat.Builder(context, id)
            .setShortLabel(shortLabel)
            .setLongLabel(longLabel)
            .setIcon(IconCompat.createWithBitmap(bitmap))
            /*.setIntent(Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url)
            ))*/
            .setIntent(Intent(App.instance.applicationContext, IntentActivity::class.java).apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(url)
            })
            .build()

        if (ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
            val callbackIntent = ShortcutManagerCompat.createShortcutResultIntent(context, shortcut)

            val successCallback = PendingIntent.getBroadcast(context, 0, callbackIntent, 0)

            ShortcutManagerCompat.requestPinShortcut(
                context,
                shortcut,
                successCallback.intentSender
            )
        }
    }

}