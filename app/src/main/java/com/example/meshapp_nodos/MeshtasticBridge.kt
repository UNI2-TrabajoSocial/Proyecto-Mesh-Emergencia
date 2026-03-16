package com.example.meshapp_nodos

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.example.meshapp_nodos.model.Node
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.Socket

data class BleDevice(
    val name: String,
    val address: String,
    val serviceUuid: String = "6ba1b218-15a8-461f-9fa8-5dcae273eafd"
)

class MeshtasticBridge {

    private var socket: Socket? = null
    private var output: OutputStream? = null
    private var reader: BufferedReader? = null
    private val uiHandler = Handler(Looper.getMainLooper())

    var bridgeConnected = mutableStateOf(false)
    var nodeConnected = mutableStateOf(false)
    
    // Rastreo del dispositivo al que intentamos conectar
    var connectedDeviceName = mutableStateOf<String?>(null)

    val devices = mutableStateListOf<BleDevice>()
    val nodes = mutableStateListOf<Node>()

    fun connect() {
        Thread {
            try {
                // Usamos 10.0.2.2 para conectar al localhost del PC desde el emulador
                socket = Socket("10.0.2.2", 4403)
                output = socket!!.getOutputStream()
                reader = BufferedReader(InputStreamReader(socket!!.getInputStream()))
                uiHandler.post { bridgeConnected.value = true }
                listen()
            } catch (e: Exception) {
                e.printStackTrace()
                uiHandler.post { bridgeConnected.value = false }
            }
        }.start()
    }

    fun scan() {
        devices.clear()
        val json = JSONObject()
        json.put("type", "scan")
        send(json.toString())
    }

    fun connectDevice(address: String) {
        // Guardamos el nombre del dispositivo si lo tenemos
        val device = devices.find { it.address == address }
        connectedDeviceName.value = device?.name ?: "Dispositivo"
        
        val json = JSONObject()
        json.put("type", "connect")
        json.put("address", address)
        send(json.toString())
    }

    fun sendMessage(message: String) {
        val json = JSONObject()
        json.put("type", "message")
        json.put("text", message)
        send(json.toString())
    }

    fun disconnect() {
        try { socket?.close() } catch (e: Exception) { e.printStackTrace() }
        socket = null
        output = null
        reader = null
        uiHandler.post {
            bridgeConnected.value = false
            nodeConnected.value = false
            connectedDeviceName.value = null
        }
    }

    private fun send(message: String) {
        Thread {
            try {
                output?.write((message + "\n").toByteArray())
                output?.flush()
            } catch (e: Exception) { e.printStackTrace() }
        }.start()
    }

    private fun listen() {
        Thread {
            try {
                while (socket != null && socket!!.isConnected) {
                    val line = reader?.readLine() ?: break
                    val json = JSONObject(line)

                    when (json.optString("type")) {

                        "scan_result" -> {
                            val arr = json.optJSONArray("devices") ?: JSONArray()
                            uiHandler.post {
                                for (i in 0 until arr.length()) {
                                    val d = arr.getJSONObject(i)
                                    val device = BleDevice(
                                        d.optString("name", "Desconocido"), 
                                        d.optString("address", "")
                                    )
                                    if (device.address.isNotEmpty() && devices.none { it.address == device.address }) {
                                        devices.add(device)
                                    }
                                }
                            }
                        }

                        "connect_result" -> {
                            val success = json.optBoolean("success", false)
                            uiHandler.post { 
                                nodeConnected.value = success 
                                // Si hay datos del nodo en el resultado de conexión, los procesamos
                                if (success && (json.has("node") || json.has("id"))) {
                                    processNodeUpdate(json.optJSONObject("node") ?: json)
                                }
                            }
                        }

                        "node_update" -> {
                            uiHandler.post {
                                processNodeUpdate(json.optJSONObject("node") ?: json)
                            }
                        }
                        
                        "node_list", "nodes" -> {
                            val arr = json.optJSONArray("nodes") ?: json.optJSONArray("devices")
                            if (arr != null) {
                                uiHandler.post {
                                    nodes.clear()
                                    for (i in 0 until arr.length()) {
                                        processNodeUpdate(arr.getJSONObject(i))
                                    }
                                }
                            }
                        }

                        "message" -> {
                            val text = json.optString("text", "")
                            println("Mensaje recibido: $text")
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                uiHandler.post { bridgeConnected.value = false }
            }
        }.start()
    }
    
    private fun processNodeUpdate(n: JSONObject) {
        val node = Node(
            n.optString("name", n.optString("id", "Nodo")),
            n.optString("id", "---"),
            n.optInt("battery", 0),
            n.optDouble("signal", 0.0),
            n.optString("distance", "---"),
            n.optString("lastSeen", "Ahora")
        )
        
        // Actualizamos o añadimos el nodo
        nodes.removeAll { it.id == node.id }
        nodes.add(node)
        
        // Si este es el nodo al que estamos conectados (por ID o nombre), 
        // actualizamos el nombre mostrado
        if (connectedDeviceName.value == null || connectedDeviceName.value == "Dispositivo") {
            connectedDeviceName.value = node.name
        }
    }
}