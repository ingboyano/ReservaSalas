package com.example.reservasaulas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListarReservasScreen(navController: NavController, reservas: List<Reserva>) {
    val customRed = Color(0xFF791414)
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TopAppBar(
            title = { Text("Listar Reservas") },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = customRed,
                titleContentColor = Color.White, // Letras del título en blanco
                navigationIconContentColor = Color.White)
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (reservas.isEmpty()) {
            Text("No hay reservas registradas.", style = MaterialTheme.typography.bodyLarge)
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(reservas) { reserva ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Aula: ${reserva.aula}")
                            Text("Fecha: ${reserva.fecha}")
                            Text("Horario: ${reserva.horario}")
                        }
                    }
                }
            }
        }
    }
}
