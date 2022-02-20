package ru.radiationx.anilibria.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.radiationx.anilibria.R
import ru.radiationx.anilibria.extension.getPrefStyleRes
import ru.radiationx.anilibria.ui.fragments.settings.SettingsFragment
import ru.radiationx.shared_app.di.injectDependencies
import tv.anilibria.module.data.preferences.AppTheme
import tv.anilibria.module.data.preferences.PreferencesStorage
import javax.inject.Inject


/**
 * Created by radiationx on 25.12.16.
 */

class SettingsActivity : BaseActivity() {

    @Inject
    lateinit var preferencesStorage: PreferencesStorage


    private lateinit var currentAppTheme: AppTheme

    override fun onCreate(savedInstanceState: Bundle?) {
        this.injectDependencies()
        currentAppTheme = preferencesStorage.appTheme.blockingGet()
        setTheme(currentAppTheme.getPrefStyleRes())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowTitleEnabled(true)
            actionBar.title = "Настройки"
        }

        supportFragmentManager.beginTransaction().replace(R.id.fragment_content, SettingsFragment())
            .commit()

        preferencesStorage.appTheme.observe().onEach { appTheme ->
            if (currentAppTheme !== appTheme) {
                currentAppTheme = appTheme
                recreate()
            }
        }.launchIn(lifecycleScope)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return true
    }

    companion object {

        fun getIntent(context: Context): Intent {
            return Intent(context, SettingsActivity::class.java)
        }
    }
}
