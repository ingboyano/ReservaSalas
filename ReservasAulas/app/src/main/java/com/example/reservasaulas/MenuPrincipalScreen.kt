package com.example.reservasaulas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// Menu Principal
@Composable
fun MenuPrincipalScreen(navController: NavController, onLogout: () -> Unit) {

    val customRed = Color(0xFF791414) // Código hexadecimal para rojo


    Box(
        modifier = Modifier
            .fillMaxSize(), // Ocupa toda la pantalla
        contentAlignment = Alignment.Center // Centra el contenido
    ) {
        // Contenedor principal
        Column(
            modifier = Modifier
                .width(350.dp) // Establece un ancho específico
                .height(500.dp) // Establece una altura específica
                .background(
                    color = Color.White, // Fondo blanco
                    shape = MaterialTheme.shapes.medium // Esquinas redondeadas
                )
                .padding(16.dp), // Espaciado interno
            verticalArrangement = Arrangement.Center, // Contenido centrado verticalmente
            horizontalAlignment = Alignment.CenterHorizontally // Contenido centrado horizontalmente
        ) {
            // Título
            Text(
                text = "¡Bienvenido a BiblioUAA!",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), // Negrita
                color = customRed,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 100.dp)
            )

            // Botón Gestión de Reservas
            Button(
                onClick = { navController.navigate("reservas") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = MaterialTheme.shapes.medium
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Icono Reservas",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Reserva de Salas",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            // Botón Gestión de Libros
            Button(
                onClick = { navController.navigate("libros") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                shape = MaterialTheme.shapes.medium
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Book,
                        contentDescription = "Icono Libros",
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Gestiona tus Libros",
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Cerrar Sesión
            Button(
                onClick = { onLogout() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .padding(top = 50.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Cerrar Sesión",
                    color = MaterialTheme.colorScheme.onError
                )
            }
        }
    }
}

