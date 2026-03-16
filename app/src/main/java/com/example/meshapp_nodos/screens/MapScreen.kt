package com.example.meshapp_nodos.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.meshapp_nodos.ui.theme.MeshAppNodosTheme
import com.example.meshapp_nodos.ui.theme.MeshDark
import com.google.maps.android.compose.*
import com.google.android.gms.maps.model.*

@Composable
fun MapScreen() {
    // Coordenadas de Talca, Chile
    val talca = LatLng(-35.4264, -71.6554)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(talca, 13f)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MeshDark)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Contenedor del mapa con bordes redondeados y borde blanco (más largo)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(650.dp) // Rectángulo más alargado
                .clip(RoundedCornerShape(24.dp))
                .border(2.dp, Color.White, RoundedCornerShape(24.dp))
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = false,
                    mapType = MapType.NORMAL
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    myLocationButtonEnabled = true
                )
            ) {
                Marker(
                    state = MarkerState(position = talca),
                    title = "Nodo Base Talca",
                    snippet = "Nodo LoRa activo - Universidad Autónoma"
                )
                
                // Ejemplo de otros nodos en Talca
                Marker(
                    state = MarkerState(position = LatLng(-35.4280, -71.6600)),
                    title = "Carlos-Node",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                )

                Marker(
                    state = MarkerState(position = LatLng(-35.4230, -71.6500)),
                    title = "Ana-Mobile",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MapScreenPreview() {
    MeshAppNodosTheme {
        MapScreen()
    }
}
