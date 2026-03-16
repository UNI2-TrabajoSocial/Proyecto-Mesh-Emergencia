package com.example.meshapp_nodos.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meshapp_nodos.MeshtasticBridge
import com.example.meshapp_nodos.ui.theme.*

@Composable
fun SettingsScreen(bridge: MeshtasticBridge, onLogout: () -> Unit = {}) {
    
    val isConnected by bridge.nodeConnected
    val isBridgeConnected by bridge.bridgeConnected
    val connectedName by bridge.connectedDeviceName
    
    // El script de Python envía el nodo conectado en la lista de nodos.
    // Intentamos buscar el que coincida con el nombre o simplemente el primero.
    val localNode = bridge.nodes.find { it.name == connectedName } ?: bridge.nodes.firstOrNull() 

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MeshDark)
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "MeshEmergencia Maule",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Universidad Autónoma de Chile",
                style = MaterialTheme.typography.bodyMedium,
                color = MeshGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Text(
                "ESTADO DEL DISPOSITIVO CONECTADO",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Bluetooth Card con info extendida
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MeshCard),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Bluetooth,
                            contentDescription = null,
                            tint = if (isConnected) MeshGreen else MeshRed,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Bluetooth BLE", color = Color.Gray, fontSize = 12.sp)
                            Text(
                                if (isConnected) "Conectado a ${connectedName ?: localNode?.name ?: "Dispositivo"}" 
                                else "Desconectado", 
                                color = Color.White, 
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    
                    if (isConnected && localNode != null) {
                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            StatusInfoItem(Icons.Default.BatteryFull, "Batería", "${localNode.battery}%", MeshGreen)
                            StatusInfoItem(Icons.Default.Wifi, "Señal (RSSI)", "${localNode.signal.toInt()} dB", MeshBlue)
                            StatusInfoItem(Icons.Default.Straighten, "Distancia", if(localNode.distance.isEmpty()) "---" else localNode.distance, MeshYellow)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "DETALLES TÉCNICOS",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            ConfigItem(Icons.Default.Dns, "ID del Nodo", if (localNode != null) localNode.id else "---")
            ConfigItem(Icons.Default.AccessTime, "Última vez visto", if (localNode?.lastSeen?.isNotEmpty() == true) localNode.lastSeen else "Ahora")
            ConfigItem(Icons.Default.SettingsInputComponent, "Estado del Bridge", if(isBridgeConnected) "Activo (PC)" else "Inactivo")
            
            Spacer(modifier = Modifier.height(32.dp))

            // Botón de Salir
            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D384D)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null, tint = MeshRed)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "SALIR",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun StatusInfoItem(icon: ImageVector, label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
        Text(label, color = Color.Gray, fontSize = 10.sp, modifier = Modifier.padding(top = 4.dp))
        Text(value, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ConfigItem(icon: ImageVector, title: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MeshCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFF252D3F), RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = MeshBlue, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = Color.Gray, fontSize = 12.sp)
                Text(value, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    MeshAppNodosTheme {
        SettingsScreen(bridge = MeshtasticBridge())
    }
}
