package com.example.meshapp_nodos.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.meshapp_nodos.ui.theme.*

@Composable
fun HomeScreen() {
    var messageText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MeshDark)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MeshBlue)
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Chat,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Chat de Emergencia",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(MeshRed),
                    contentAlignment = Alignment.Center
                ) {
                    Text("8", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Area de Chat con el "Rectángulo Alargado" (Bordes redondeados y borde blanco)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(24.dp))
                .border(2.dp, Color.White, RoundedCornerShape(24.dp))
                .background(MeshCard.copy(alpha = 0.5f)) // Fondo sutil para el área de mensajes
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ChatMessage("Sistema", "Conectado al nodo base Talca.", false)
                ChatMessage("Carlos-Node", "Hola equipo, ¿cómo está la señal por allá?", true)
                ChatMessage("Ana-Mobile", "Todo bien por aquí, señal estable.", true)
                ChatMessage("Mi Dispositivo", "Recibido. Estamos monitoreando los saltos.", false)
            }
        }

        // Bottom input bar
        Surface(
            color = Color.Transparent, // Hacemos transparente para que no rompa la estética
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text("Escribe un mensaje...", color = Color.Gray) },
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MeshCard,
                        unfocusedContainerColor = MeshCard,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                IconButton(
                    onClick = { if(messageText.isNotBlank()) messageText = "" },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MeshBlue)
                ) {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Enviar",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun ChatMessage(sender: String, message: String, isOthers: Boolean) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isOthers) Alignment.Start else Alignment.End
    ) {
        Text(
            text = sender,
            color = if (isOthers) MeshBlue else MeshGreen,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        Surface(
            color = if (isOthers) Color(0xFF2D384D) else MeshBlue,
            shape = RoundedCornerShape(
                topStart = 12.dp,
                topEnd = 12.dp,
                bottomStart = if (isOthers) 0.dp else 12.dp,
                bottomEnd = if (isOthers) 12.dp else 0.dp
            )
        ) {
            Text(
                text = message,
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MeshAppNodosTheme {
        HomeScreen()
    }
}
