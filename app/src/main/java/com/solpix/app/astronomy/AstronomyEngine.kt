package com.solpix.app.astronomy

import net.e175.klaus.solarpositioning.SunAndMoonPosition
import net.e175.klaus.solarpositioning.SunAndMoonTimes
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

data class CelestialPosition(val x: Float, val y: Float, val isVisible: Boolean)

class AstronomyEngine {
    fun calculatePositions(lat: Double, lon: Double, dateTime: ZonedDateTime): Pair<CelestialPosition, CelestialPosition> {
        val sunPos = SunAndMoonPosition.getPosition(dateTime.toInstant(), lat, lon)
        val sunX = (sunPos.azimuth / 360.0).toFloat()
        val sunY = (1.0 - (sunPos.altitude / 90.0)).toFloat()
        val sunVisible = sunPos.altitude > 0

        val moonPos = SunAndMoonPosition.getMoonPosition(dateTime.toInstant(), lat, lon)
        val moonX = (moonPos.azimuth / 360.0).toFloat()
        val moonY = (1.0 - (moonPos.altitude / 90.0)).toFloat()
        val moonVisible = moonPos.altitude > 0

        return Pair(CelestialPosition(sunX, sunY, sunVisible), CelestialPosition(moonX, moonY, moonVisible))
    }
    
    fun getSunTimes(lat: Double, lon: Double, date: LocalDate, zoneId: ZoneId): Pair<LocalTime?, LocalTime?> {
        val times = SunAndMoonTimes.getSunTimes(date, lat, lon, zoneId)
        return Pair(times.sunrise?.toLocalTime(), times.sunset?.toLocalTime())
    }

    fun getMoonTimes(lat: Double, lon: Double, date: LocalDate, zoneId: ZoneId): Pair<LocalTime?, LocalTime?> {
        val times = SunAndMoonTimes.getMoonTimes(date, lat, lon, zoneId)
        return Pair(times.moonrise?.toLocalTime(), times.moonset?.toLocalTime())
    }
}