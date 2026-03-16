package com.example.meshapp_nodos.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

@Composable
fun BottomBar(
    selected: Int,
    onSelect: (Int) -> Unit
) {
    NavigationBar(
        containerColor = Color(0xFF111926), // Fondo oscuro de la barra
        contentColor = Color.White
    ) {
        val items = listOf(
            Triple("Mensajes", Icons.AutoMirrored.Filled.Chat, 0),
            Triple("Nodos", Icons.Default.GridView, 1),
            Triple("Mapa", Icons.Default.Map, 2),
            Triple("Ajustes", Icons.Default.Settings, 3)
        )

        items.forEach { (label, icon, index) ->
            val isSelected = selected == index
            NavigationBarItem(
                selected = isSelected,
                onClick = { onSelect(index) },
                label = { 
                    Text(
                        text = label, 
                        color = if (isSelected) Color.White else Color.Gray 
                    ) 
                },
                icon = { 
                    Icon(
                        imageVector = icon, 
                        contentDescription = label,
                        tint = if (isSelected) Color.White else Color.Gray
                    ) 
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent, // Quita el círculo/píldora de selección
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.Gray
                ),
                interactionSource = remember { MutableInteractionSource() } // Ayuda a manejar la interacción sin efectos extra
            )
        }
    }
}
