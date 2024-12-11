@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.reservasaulas

import RetrofitClient
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Composable para listar reservas
@Composable
fun ListarReservasScreen(navController: NavController, reservas: List<Reserva>) {
    val context = LocalContext.current
    val reservas = remember { mutableStateListOf<Reserva>() }
    val isLoading = remember { mutableStateOf(true) }
    val errorMessage = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(true) {
        // Realizamos la llamada para obtener las reservas
        RetrofitClient.reservaApi.getAllReservas().enqueue(object : Callback<List<Reserva>> {
            override fun onResponse(call: Call<List<Reserva>>, response: Response<List<Reserva>>) {
                if (response.isSuccessful) {
                    // Si la respuesta es exitosa, actualizamos la lista de reservas
                    reservas.clear()
                    reservas.addAll(response.body() ?: emptyList())
                    isLoading.value = false
                } else {
                    // Si la respuesta no es exitosa, mostramos un mensaje de error
                    isLoading.value = false
                    errorMessage.value = "Error al cargar las reservas"
                }
            }

            override fun onFailure(call: Call<List<Reserva>>, t: Throwable) {
                // Si ocurre un error durante la solicitud, mostramos un mensaje
                isLoading.value = false
                errorMessage.value = "Error de red: ${t.message}"
            }
        })
    }

    val customRed = Color(0xFF791414)
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TopAppBar(
            title = { Text("Listar Reservas") },
            navigationIcon = {
                IconButton(onClick = { navController.navigate("Reservas") }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver a Registro de Reservas")
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = customRed,
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Muestra un mensaje de carga
        if (isLoading.value) {
            Text("Cargando reservas...", style = MaterialTheme.typography.bodyLarge)
        }

        // Muestra un mensaje de error si falla la carga
        if (errorMessage.value != null) {
            Text("Error: ${errorMessage.value}", style = MaterialTheme.typography.bodyLarge, color = Color.Red)
        }

        // Si no hay reservas, muestra mensaje
        if (reservas.isEmpty() && !isLoading.value) {
            Text("No hay reservas registradas.", style = MaterialTheme.typography.bodyLarge)
        } else {
            // Mostramos las reservas en una lista
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(reservas) { reserva ->
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Aula: ${reserva.aula}", style = MaterialTheme.typography.bodyLarge)
                            Text("Fecha: ${reserva.fecha}", style = MaterialTheme.typography.bodyMedium)
                            Text("Horario: ${reserva.horario}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}
