@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.reservasaulas

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
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

    var expanded by remember { mutableStateOf(false) } // Para controlar el estado de expansión
    var errorAula by remember { mutableStateOf(false) }
    var errorFecha by remember { mutableStateOf(false) }
    var errorHoraInicio by remember { mutableStateOf(false) }

    // Lista de opciones de aulas
    val aulas = listOf("Aula 1", "Aula 2", "Aula 3", "Aula 4", "Aula 5", "Aula 6")
    var selectedAulas by remember { mutableStateOf(listOf<String>()) }

    // Expresión regular para validar el formato de la fecha (dd/MM/yyyy)
    val fechaRegex = """^\d{2}/\d{2}/\d{4}$""".toRegex()

    // Expresión regular para validar el formato de la hora (HH:mm)
    val horaRegex = """^([01]?[0-9]|2[0-3]):([0-5]?[0-9])$""".toRegex()

    // Validar la fecha
    fun validarFecha(fecha: String): Boolean {
        return fechaRegex.matches(fecha)
    }

    // Validar la hora
    fun validarHora(hora: String): Boolean {
        return horaRegex.matches(hora)
    }

    // Cargar el usuarioId desde SharedPreferences cuando la pantalla se inicializa
    LaunchedEffect(key1 = usuarioId) {
        if (usuarioId != 0) {
            RetrofitClient.usuarioApi.getUsuarioById(usuarioId).enqueue(object : retrofit2.Callback<Usuario> {
                override fun onResponse(call: retrofit2.Call<Usuario>, response: retrofit2.Response<Usuario>) {
                    if (response.isSuccessful) {
                        usuarioLogueado = response.body()
                        Log.d("API_DEBUG", "Usuario logueado: ${usuarioLogueado?.nombreUsuario}")
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
                    Text("Menú Principal", color = Color.White)
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = customRed,
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White)
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

                // Aula (Multiselección)
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedAulas.joinToString(", "),
                        onValueChange = { /* No hacer nada, solo mostrar la selección */ },
                        label = { Text("Aulas") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        }
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        aulas.forEach { aulaOption ->
                            DropdownMenuItem(onClick = {
                                selectedAulas = selectedAulas.toMutableList().apply {
                                    if (!contains(aulaOption)) add(aulaOption)
                                }
                                expanded = false // Cerrar el menú después de seleccionar
                            }) {
                                Text(aulaOption)
                            }
                        }
                    }
                }

                if (errorAula) {
                    Text("Debe seleccionar al menos un aula", color = MaterialTheme.colorScheme.error)
                }

                // Campo de Fecha
                OutlinedTextField(
                    value = fecha,
                    onValueChange = {
                        fecha = it
                        errorFecha = !validarFecha(it)
                    },
                    label = { Text("Fecha (dd/MM/yyyy)") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = errorFecha,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = customRed,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                )
                if (errorFecha) {
                    Text("Formato de fecha inválido. Use dd/MM/yyyy", color = MaterialTheme.colorScheme.error)
                }

                // Campo de Hora de Inicio
                OutlinedTextField(
                    value = horaInicio,
                    onValueChange = {
                        horaInicio = it
                        errorHoraInicio = !validarHora(it)
                    },
                    label = { Text("Hora de Inicio (HH:mm)") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = errorHoraInicio,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = customRed,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                )
                if (errorHoraInicio) {
                    Text("Formato de hora inválido. Use HH:mm", color = MaterialTheme.colorScheme.error)
                }

                // Botón de confirmación o validación
                Button(
                    onClick = {
                        if (selectedAulas.isEmpty() || errorFecha || errorHoraInicio) {
                            // Mostrar un mensaje de error si hay campos inválidos
                            Toast.makeText(context, "Por favor corrija los errores", Toast.LENGTH_SHORT).show()
                        } else {
                            // Proceder con la reserva
                            // Implementar lógica de reserva aquí
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = customRed)
                ) {
                    Text("Confirmar Reserva", color = Color.White)
                }
            }
        }
    }
}