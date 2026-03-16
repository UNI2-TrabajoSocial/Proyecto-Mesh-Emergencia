package com.example.meshapp_nodos.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meshapp_nodos.MeshtasticBridge
import com.example.meshapp_nodos.components.NodeCard
import com.example.meshapp_nodos.ui.theme.*

@Composable
fun NodesScreen(bridge: MeshtasticBridge) {

    var isScanning by remember { mutableStateOf(false) }

    val isConnected by bridge.nodeConnected
    val isBridgeConnected by bridge.bridgeConnected

    // 🔵 DATOS REALES DESDE EL BRIDGE (SnapshotStateList se observa automáticamente)
    val availableDevices = bridge.devices
    val nodes = bridge.nodes

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MeshDark)
            .padding(16.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {

            Icon(
                Icons.Default.GridView,
                contentDescription = null,
                tint = MeshGreen,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Nodos",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(MeshGreen),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    "${nodes.size}",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "ESTADO DEL DISPOSITIVO",
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 8.dp)
        )

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
                        if (isScanning) Icons.Default.BluetoothSearching else Icons.Default.Bluetooth,
                        contentDescription = null,
                        tint = if (isConnected) MeshGreen else MeshRed,
                        modifier = Modifier.size(32.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1.1f)) {

                        Text(
                            "Bluetooth BLE Connection",
                            color = Color.Gray,
                            fontSize = 10.sp
                        )

                        Text(
                            if (isConnected)
                                "Conectado"
                            else if (isScanning)
                                "Buscando..."
                            else
                                "Desconectado",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(

                        onClick = {

                            if (isConnected) {

                                bridge.disconnect()
                                isScanning = false

                            } else {

                                if (!isScanning) {
                                    isScanning = true
                                    bridge.scan()
                                } else {
                                    isScanning = false
                                }

                            }

                        },

                        colors = ButtonDefaults.buttonColors(
                            containerColor =
                                if (isConnected) MeshRed else MeshBlue
                        ),

                        shape = RoundedCornerShape(8.dp),

                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)

                    ) {

                        Text(
                            if (isConnected) "Desconectar"
                            else if (isScanning) "Cancelar"
                            else "Buscar",
                            color = Color.White
                        )
                    }
                }
                
                if (!isBridgeConnected) {
                    Text(
                        "Error: No hay conexión con el PC (Bridge)",
                        color = MeshRed,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

        // 🔵 LISTA DE DISPOSITIVOS ENCONTRADOS
        AnimatedVisibility(visible = isScanning) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {

                Text(
                    "DISPOSITIVOS ENCONTRADOS",
                    color = MeshBlue,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp, start = 8.dp)
                )
                
                if (availableDevices.isEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = MeshBlue, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Buscando dispositivos...", color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                } else {
                    LazyColumn(modifier = Modifier.heightIn(max = 250.dp)) {

                        items(availableDevices) { device ->

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = MeshCard.copy(alpha = 0.8f)),
                                shape = RoundedCornerShape(12.dp)
                            ) {

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Column(modifier = Modifier.weight(1f)) {

                                        Text(
                                            text = device.name.ifEmpty { "Dispositivo desconocido" },
                                            color = Color.White,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        )

                                        Text(
                                            text = device.address,
                                            color = Color.Gray,
                                            fontSize = 11.sp
                                        )
                                    }

                                    Button(

                                        onClick = {

                                            bridge.connectDevice(device.address)
                                            isScanning = false

                                        },

                                        colors = ButtonDefaults.buttonColors(containerColor = MeshBlue),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.height(36.dp)

                                    ) {

                                        Text(
                                            "Conectar",
                                            color = Color.White,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            "NODOS ACTIVOS EN LA RED",
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {

            items(nodes) { node ->

                NodeCard(node)

            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun NodesScreenPreview() {

    MeshAppNodosTheme {

        NodesScreen(bridge = MeshtasticBridge())

    }
}