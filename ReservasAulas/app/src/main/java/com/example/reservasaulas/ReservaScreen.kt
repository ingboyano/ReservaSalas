@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.reservasaulas

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ReservasScreen(navController: NavController, reservas: List<Reserva>) {
    val customRed = Color(0xFF791414) // Color rojo
    val context = LocalContext.current // Contexto para Toast
    var aula by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var horaInicio by remember { mutableStateOf("") }
    var usuarioId by remember { mutableStateOf(0) }  // Inicializamos el ID del usuario como 0
    var usuarioLogueado by remember { mutableStateOf<Usuario?>(null) } // Usuario logueado

    var errorAula by remember { mutableStateOf(false) }
    var errorFecha by remember { mutableStateOf(false) }
    var errorHoraInicio by remember { mutableStateOf(false) }

    // Obtener usuario logueado
    LaunchedEffect(key1 = usuarioId) {
        if (usuarioId != 0) {
            RetrofitClient.usuarioApi.getUsuarioById(usuarioId).enqueue(object : retrofit2.Callback<Usuario> {
                override fun onResponse(call: retrofit2.Call<Usuario>, response: retrofit2.Response<Usuario>) {
                    if (response.isSuccessful) {
                        usuarioLogueado = response.body()
                    } else {
                        Log.e("API_ERROR", "No se pudo obtener el usuario")
                    }
                }

                override fun onFailure(call: retrofit2.Call<Usuario>, t: Throwable) {
                    Log.e("API_ERROR", "Fallo en la conexión: ${t.message}")
                }
            })
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopAppBar(
            title = { Text("Gestionar Reservas") },
            actions = {
                TextButton(onClick = { navController.navigate("menu") }) {
                    Text("Menú Principal", color = customRed)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))


        // Botones de navegación
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { navController.navigate("listarReservas") },
                colors = ButtonDefaults.buttonColors(containerColor = customRed),
                modifier = Modifier.weight(1f)
            ) {
                Text("Listar Reservas", color = MaterialTheme.colorScheme.onPrimary)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { navController.navigate("cancelarReservas") },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancelar Reservas", color = MaterialTheme.colorScheme.onSecondary)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Registrar Nueva Reserva", style = MaterialTheme.typography.headlineSmall)

                // Aula
                OutlinedTextField(
                    value = aula,
                    onValueChange = {
                        aula = it
                        errorAula = aula.isEmpty()
                    },
                    label = { Text("Aula") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = errorAula,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = customRed,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                )
                if (errorAula) {
                    Text("El aula no puede estar vacía", color = MaterialTheme.colorScheme.error)
                }

                // Fecha
                OutlinedTextField(
                    value = fecha,
                    onValueChange = {
                        fecha = it
                        errorFecha = fecha.isEmpty()
                    },
                    label = { Text("Fecha") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = errorFecha,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = customRed,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                )
                if (errorFecha) {
                    Text("La fecha no puede estar vacía", color = MaterialTheme.colorScheme.error)
                }

                // Hora Inicio
                OutlinedTextField(
                    value = horaInicio,
                    onValueChange = {
                        horaInicio = it
                        errorHoraInicio = horaInicio.isEmpty()
                    },
                    label = { Text("Horario") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = errorHoraInicio,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = customRed,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                )
                if (errorHoraInicio) {
                    Text("La hora de inicio no puede estar vacía", color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón Registrar
                Button(
                    onClick = {
                            val nuevaReserva = Reserva(aula, fecha, horaInicio, usuarioId)
                            RetrofitClient.reservaApi.createReserva(nuevaReserva).enqueue(object : retrofit2.Callback<Reserva> {
                                override fun onResponse(call: retrofit2.Call<Reserva>, response: retrofit2.Response<Reserva>) {
                                    if (response.isSuccessful) {
                                        aula = ""
                                        fecha = ""
                                        horaInicio = ""
                                        Toast.makeText(context, "Reserva registrada con éxito", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Log.e("API_ERROR", "Error: ${response.errorBody()?.string()}")
                                    }
                                }
                                override fun onFailure(call: retrofit2.Call<Reserva>, t: Throwable) {
                                    Log.e("API_ERROR", "Fallo en la conexión: ${t.message}")
                                    Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show()
                                }
                            })
                        },
                    enabled = aula.isNotEmpty() && fecha.isNotEmpty() && horaInicio.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(containerColor = customRed),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Registrar Reserva", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}

