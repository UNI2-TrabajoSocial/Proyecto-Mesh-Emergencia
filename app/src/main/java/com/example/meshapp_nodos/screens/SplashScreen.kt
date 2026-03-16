package com.example.meshapp_nodos.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.RssFeed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meshapp_nodos.R
import com.example.meshapp_nodos.ui.theme.MeshBlue
import com.example.meshapp_nodos.ui.theme.MeshRed
import com.example.meshapp_nodos.ui.theme.MeshYellow

@Composable
fun SplashScreen(onEnter: () -> Unit) {
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    0.0f to MeshRed,
                    0.4f to MeshRed,
                    1.0f to MeshBlue
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .alpha(alpha.value)
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Logo de la universidad con bordes redondeados y tamaño menor
            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = "Logo Universidad",
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(24.dp))
            )
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // Titulo: MeshEmergencia Maule
            Text(
                text = "MeshEmergencia Maule",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Subtitulo: Sistema de Comunicación de Emergencia
            Text(
                text = "Sistema de Comunicación de Emergencia",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(60.dp))

            // Botón de Ingresar
            Button(
                onClick = onEnter,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = MeshBlue
                ),
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(56.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "INGRESAR",
                        color = Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }

        // Texto pequeño al fondo
        Text(
            text = "v1.0.0 - Universidad Autónoma de Chile",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    // Como la función original pide un 'onEnter', aquí pasamos uno vacío {}
    SplashScreen(onEnter = {})
}
