package ru.radiationx.anilibria.ui.activities.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import ru.radiationx.anilibria.presentation.common.ILinkHandler
import ru.radiationx.anilibria.ui.activities.BaseActivity
import ru.radiationx.shared_app.AppLinkHelper
import ru.radiationx.shared_app.di.injectDependencies
import tv.anilibria.core.types.AbsoluteUrl
import javax.inject.Inject

/**
 * Created by radiationx on 23.02.18.
 */
class IntentActivity : BaseActivity() {

    @Inject
    lateinit var linkHandler: ILinkHandler

    @Inject
    lateinit var appLinkHelper: AppLinkHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
        Log.e("lalala", "IntentActivity intent: $intent")
        intent?.data?.also { intentUri ->
            val screen = linkHandler.findScreen(intentUri.toString())
            Log.e("lalala", "screen: $screen, url=$intentUri")
            if (screen != null) {
                startActivity(Intent(this@IntentActivity, MainActivity::class.java).apply {
                    data = intentUri
                })
            } else {
                appLinkHelper.openLink(AbsoluteUrl(intentUri.toString()))
            }
        }
        finish()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.e("lalala", "IntentActivity onnewintent $intent")
    }
}