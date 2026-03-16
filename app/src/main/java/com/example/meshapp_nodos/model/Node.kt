package com.example.meshapp_nodos.model

data class Node(
    val name: String,
    val id: String,
    val battery: Int,
    val signal: Double,
    val distance: String,
    val lastSeen: String
)