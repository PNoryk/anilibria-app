package ru.radiationx.anilibria.di

import android.content.Context
import ru.radiationx.anilibria.AppBuildConfig
import ru.radiationx.anilibria.AppMigrationExecutor
import ru.radiationx.anilibria.TvCheckerSources
import ru.radiationx.shared_app.analytics.errors.AppMetricaErrorReporter
import ru.radiationx.shared_app.analytics.errors.CombinedErrorReporter
import ru.radiationx.shared_app.analytics.errors.LoggingErrorReporter
import ru.radiationx.shared_app.analytics.events.AppMetricaAnalyticsSender
import ru.radiationx.shared_app.analytics.events.CombinedAnalyticsSender
import ru.radiationx.shared_app.analytics.events.LoggingAnalyticsSender
import ru.radiationx.shared_app.analytics.profile.AppMetricaAnalyticsProfile
import ru.radiationx.shared_app.analytics.profile.CombinedAnalyticsProfile
import ru.radiationx.shared_app.analytics.profile.LoggingAnalyticsProfile
import ru.radiationx.shared_app.common.OkHttpImageDownloader
import toothpick.config.Module
import tv.anilibria.feature.appupdates.data.CheckerReserveSources
import tv.anilibria.feature.data.migration.MigrationExecutor
import tv.anilibria.plugin.data.analytics.AnalyticsErrorReporter
import tv.anilibria.plugin.data.analytics.AnalyticsSender
import tv.anilibria.plugin.data.analytics.profile.AnalyticsProfile
import tv.anilibria.plugin.shared.appinfo.SharedBuildConfig

class AppModule(context: Context) : Module() {


    init {
        bind(Context::class.java).toInstance(context)
        bind(SharedBuildConfig::class.java).to(AppBuildConfig::class.java).singleton()
        bind(CheckerReserveSources::class.java).to(TvCheckerSources::class.java).singleton()
        bind(MigrationExecutor::class.java).to(AppMigrationExecutor::class.java).singleton()

        bind(OkHttpImageDownloader::class.java).singleton()

        bind(AppMetricaAnalyticsSender::class.java).singleton()
        bind(AppMetricaAnalyticsProfile::class.java).singleton()
        bind(AppMetricaErrorReporter::class.java).singleton()

        bind(LoggingAnalyticsSender::class.java).singleton()
        bind(LoggingAnalyticsProfile::class.java).singleton()
        bind(LoggingErrorReporter::class.java).singleton()

        bind(AnalyticsSender::class.java).to(CombinedAnalyticsSender::class.java).singleton()
        bind(AnalyticsProfile::class.java).to(CombinedAnalyticsProfile::class.java).singleton()
        bind(AnalyticsErrorReporter::class.java).to(CombinedErrorReporter::class.java).singleton()
    }

}