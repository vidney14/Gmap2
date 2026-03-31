package com.example.gmap2

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gmap2.ui.theme.Gmap2Theme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Gmap2Theme {
                OutdoorExplorerView()
            }
        }
    }
}

@Composable
fun OutdoorExplorerView() {
    val appCtx = LocalContext.current
    
    // Customization states with updated names
    var routeTint by remember { mutableStateOf(Color.Magenta) }
    var routeSize by remember { mutableFloatStateOf(12f) }
    var zoneTint by remember { mutableStateOf(Color.Cyan.copy(alpha = 0.35f)) }
    var zoneBorderWidth by remember { mutableFloatStateOf(6f) }

    // Updated coordinates for London (Hyde Park area)
    val hikePath = listOf(
        LatLng(51.5055, -0.1750),
        LatLng(51.5065, -0.1720),
        LatLng(51.5075, -0.1680),
        LatLng(51.5085, -0.1640),
        LatLng(51.5095, -0.1600)
    )
    
    val reserveArea = listOf(
        LatLng(51.5030, -0.1600),
        LatLng(51.5080, -0.1600),
        LatLng(51.5080, -0.1500),
        LatLng(51.5030, -0.1500)
    )

    val viewState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(51.5072, -0.1657), 14.5f)
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = viewState
            ) {
                // Trail implementation
                Polyline(
                    points = hikePath,
                    clickable = true,
                    color = routeTint,
                    width = routeSize,
                    onClick = {
                        Toast.makeText(appCtx, "Route: Kensington Garden Walk", Toast.LENGTH_SHORT).show()
                    }
                )

                // Park implementation
                Polygon(
                    points = reserveArea,
                    clickable = true,
                    fillColor = zoneTint,
                    strokeColor = Color.DarkGray,
                    strokeWidth = zoneBorderWidth,
                    onClick = {
                        Toast.makeText(appCtx, "Zone: Hyde Park Reserve", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            // Floating Customization Panel
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                    .padding(16.dp)
            ) {
                Text(
                    text = "Trail Settings", 
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { routeTint = Color.Magenta }) {
                        Box(Modifier.size(24.dp).background(Color.Magenta))
                    }
                    IconButton(onClick = { routeTint = Color.Black }) {
                        Box(Modifier.size(24.dp).background(Color.Black))
                    }
                    Slider(
                        value = routeSize,
                        onValueChange = { routeSize = it },
                        valueRange = 4f..40f,
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Park Settings", 
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { zoneTint = Color.Cyan.copy(alpha = 0.35f) }) {
                        Box(Modifier.size(24.dp).background(Color.Cyan))
                    }
                    IconButton(onClick = { zoneTint = Color.Red.copy(alpha = 0.35f) }) {
                        Box(Modifier.size(24.dp).background(Color.Red))
                    }
                    Slider(
                        value = zoneBorderWidth,
                        onValueChange = { zoneBorderWidth = it },
                        valueRange = 1f..15f,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
