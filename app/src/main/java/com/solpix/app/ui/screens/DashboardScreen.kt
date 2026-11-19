package com.solpix.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.solpix.app.astronomy.AstronomyEngine
import com.solpix.app.data.SettingsRepository
import com.solpix.app.widget.WidgetRenderer
import kotlinx.coroutines.flow.first
import java.time.ZonedDateTime

@Composable
fun DashboardScreen() {
    val context = LocalContext.current
    val repo = SettingsRepository(context)
    val engine = AstronomyEngine()
    
    var lat by remember { mutableStateOf(0.0) }
    var lon by remember { mutableStateOf(0.0) }
    
    LaunchedEffect(Unit) {
        val loc = repo.locationFlow.first()
        lat = loc.first
        lon = loc.second
    }

    val currentTime = ZonedDateTime.now()
    val (sunPos, moonPos) = engine.calculatePositions(lat, lon, currentTime)
    val (sunrise, sunset) = engine.getSunTimes(lat, lon, currentTime.toLocalDate(), currentTime.zone)
    val (moonrise, moonset) = engine.getMoonTimes(lat, lon, currentTime.toLocalDate(), currentTime.zone)

    val renderer = WidgetRenderer(context)
    val sceneBitmap = renderer.render(800, 400, sunPos, moonPos, sunrise, sunset, currentTime.toLocalTime())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("SOLPIX", fontSize = 32.sp, color = Color(0xFFD4AF37))
        Spacer(modifier = Modifier.height(16.dp))
        
        Image(bitmap = sceneBitmap.asImageBitmap(), contentDescription = "Scene", modifier = Modifier.fillMaxWidth().height(200.dp))
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Location: Lat: $lat, Lon: $lon", color = Color.White)
                Text("Sunrise: ${sunrise ?: "N/A"} | Sunset: ${sunset ?: "N/A"}", color = Color(0xFFD4AF37))
                Text("Moonrise: ${moonrise ?: "N/A"} | Moonset: ${moonset ?: "N/A"}", color = Color(0xFFADD8E6))
            }
        }
    }
}