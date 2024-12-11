@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.reservasaulas

import RetrofitClient
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

// Pantalla para cancelar/eliminar reservas
@Composable
fun CancelarReservasScreen(navController: NavController, reservas: List<Reserva>) {
    val reservas = remember { mutableStateListOf<Reserva>() }
    val errorMessage = remember { mutableStateOf("") }
    val isLoading = remember { mutableStateOf(true) }

    // Llamada a la API para obtener las reservas
    LaunchedEffect(Unit) {
        val api = RetrofitClient.retrofit.create(ReservaApi::class.java)
        api.getAllReservas().enqueue(object : Callback<List<Reserva>> {
            override fun onResponse(call: Call<List<Reserva>>, response: Response<List<Reserva>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        reservas.clear()
                        reservas.addAll(it)
                    }
                    isLoading.value = false
                } else {
                    errorMessage.value = "Error al cargar las reservas: ${response.code()} ${response.message()}"
                    isLoading.value = false
                }
            }

            override fun onFailure(call: Call<List<Reserva>>, t: Throwable) {
                if (t is IOException) {
                    errorMessage.value = "Error de red al cargar las reservas: ${t.localizedMessage}"
                } else {
                    errorMessage.value = "Error desconocido: ${t.localizedMessage}"
                }
                isLoading.value = false
            }
        })
    }

    val customRed = Color(0xFF791414)
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TopAppBar(
            title = { Text("Cancelar Reserva") },
            navigationIcon = {
                IconButton(onClick = { navController.navigate("reservas") }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver a Reservas")
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = customRed,
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Si hay error, mostrar el mensaje
        if (errorMessage.value.isNotEmpty()) {
            Text("Error: ${errorMessage.value}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
        }

        // Si estamos cargando
        if (isLoading.value) {
            Text("Cargando reservas...", style = MaterialTheme.typography.bodyLarge)
        } else {
            // Mostrar reservas si hay datos disponibles
            if (reservas.isEmpty()) {
                Text("No hay reservas para cancelar.", style = MaterialTheme.typography.bodyLarge)
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(reservas) { reserva ->
                        // Aquí agregamos el fondo blanco debajo de cada reserva
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Aula: ${reserva.aula}", style = MaterialTheme.typography.bodyLarge)
                                    Text("Fecha: ${reserva.fecha}", style = MaterialTheme.typography.bodyMedium)
                                    Text("Horario: ${reserva.horario}", style = MaterialTheme.typography.bodyMedium)
                                }
                                Button(
                                    onClick = {
                                        val api = RetrofitClient.retrofit.create(ReservaApi::class.java)
                                        api.deleteReservaByAulaAndHorario(reserva.aula, reserva.horario).enqueue(object : Callback<String> {
                                            override fun onResponse(call: Call<String>, response: Response<String>) {
                                                if (response.isSuccessful) {
                                                    // Eliminar la reserva de la lista solo si la API confirma la eliminación
                                                    reservas.remove(reserva)
                                                } else {
                                                    // Mostrar error solo si la eliminación no fue exitosa
                                                    errorMessage.value = "No se pudo cancelar la reserva: ${response.code()} ${response.message()}"
                                                }
                                            }

                                            override fun onFailure(call: Call<String>, t: Throwable) {
                                                if (t is IOException) {
                                                    errorMessage.value = "Error de red al cancelar la reserva: ${t.localizedMessage}"
                                                } else {
                                                    errorMessage.value = "Error desconocido: ${t.localizedMessage}"
                                                }
                                            }
                                        })
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                                    modifier = Modifier.padding(start = 8.dp)
                                ) {
                                    Text("Cancelar", color = MaterialTheme.colorScheme.onError)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}