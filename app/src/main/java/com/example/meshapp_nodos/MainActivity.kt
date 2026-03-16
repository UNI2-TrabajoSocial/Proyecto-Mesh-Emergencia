package com.example.meshapp_nodos

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.meshapp_nodos.components.BottomBar
import com.example.meshapp_nodos.screens.*
import com.example.meshapp_nodos.ui.theme.MeshAppNodosTheme
import com.example.meshapp_nodos.ui.theme.MeshDark

class MainActivity : ComponentActivity() {

    // Bridge hacia Meshtastic / Python
    private lateinit var bridge: MeshtasticBridge

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // Inicializar bridge
        bridge = MeshtasticBridge()

        // Conectar al bridge
        bridge.connect()

        // 🔵 Enviar mensaje de prueba al bridge
        Thread {

            try {

                Thread.sleep(3000)

                bridge.sendMessage("hola desde android")

            } catch (e: Exception) {

                e.printStackTrace()

            }

        }.start()

        setContent {

            MeshAppNodosTheme {

                var showSplash by remember { mutableStateOf(true) }

                if (showSplash) {

                    SplashScreen(
                        onEnter = {
                            showSplash = false
                        }
                    )

                } else {

                    var screen by remember { mutableStateOf(0) }

                    Scaffold(
                        containerColor = MeshDark,
                        bottomBar = {
                            BottomBar(screen) {
                                screen = it
                            }
                        }
                    ) { padding ->

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                        ) {

                            when (screen) {

                                0 -> HomeScreen()

                                1 -> NodesScreen(bridge)

                                2 -> MapScreen()

                                3 -> SettingsScreen(
                                    bridge = bridge,
                                    onLogout = {
                                        showSplash = true
                                        screen = 0
                                    }
                                )

                            }

                        }

                    }

                }

            }

        }

    }

}