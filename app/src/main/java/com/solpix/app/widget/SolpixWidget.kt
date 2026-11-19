package com.solpix.app.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.fillMaxSize
import com.solpix.app.astronomy.AstronomyEngine
import com.solpix.app.data.SettingsRepository
import kotlinx.coroutines.flow.first
import java.time.ZonedDateTime

class SolpixWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val repo = SettingsRepository(context)
        val (lat, lon) = repo.locationFlow.first()
        val engine = AstronomyEngine()
        val now = ZonedDateTime.now()
        
        val (sunPos, moonPos) = engine.calculatePositions(lat, lon, now)
        val (sunrise, sunset) = engine.getSunTimes(lat, lon, now.toLocalDate(), now.zone)

        val renderer = WidgetRenderer(context)
        val bitmap = renderer.render(600, 300, sunPos, moonPos, sunrise, sunset, now.toLocalTime())

        provideContent {
            Image(
                provider = ImageProvider(bitmap),
                contentDescription = "Solpix Scene",
                modifier = GlanceModifier.fillMaxSize()
            )
        }
    }
}