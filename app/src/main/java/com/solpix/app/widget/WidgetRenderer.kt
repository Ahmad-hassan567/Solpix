package com.solpix.app.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import com.solpix.app.R
import com.solpix.app.astronomy.CelestialPosition
import java.time.LocalTime

class WidgetRenderer(private val context: Context) {
    fun render(width: Int, height: Int, sunPos: CelestialPosition, moonPos: CelestialPosition, sunrise: LocalTime?, sunset: LocalTime?, currentTime: LocalTime): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply { isFilterBitmap = false }

        val isDay = currentTime.isAfter(sunrise ?: LocalTime.MIN) && currentTime.isBefore(sunset ?: LocalTime.MAX)
        
        val bgRes = when {
            isDay -> R.drawable.bg_morning
            currentTime.isAfter((sunset ?: LocalTime.MAX).minusMinutes(60)) -> R.drawable.bg_evening
            else -> R.drawable.bg_night
        }
        
        try {
            val bgBitmap = BitmapFactory.decodeResource(context.resources, bgRes)
            val scaledBg = Bitmap.createScaledBitmap(bgBitmap, width, height, false)
            canvas.drawBitmap(scaledBg, 0f, 0f, paint)
        } catch (e: Exception) {
            canvas.drawColor(android.graphics.Color.DKGRAY)
        }

        if (sunPos.isVisible) {
            try {
                val sunBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pixel_sun)
                canvas.drawBitmap(sunBitmap, sunPos.x * width - 25, sunPos.y * height - 25, paint)
            } catch (e: Exception) {}
        }

        if (moonPos.isVisible) {
            try {
                val moonBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pixel_moon)
                canvas.drawBitmap(moonBitmap, moonPos.x * width - 20, moonPos.y * height - 20, paint)
            } catch (e: Exception) {}
        }

        return bitmap
    }
}