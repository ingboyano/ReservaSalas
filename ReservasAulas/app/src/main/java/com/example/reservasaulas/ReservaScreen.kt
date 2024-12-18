
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
    // Definición del color rojo personalizado
    val customRed = Color(0xFF791414) // Color rojo

    // Contexto actual para mostrar Toasts
    val context = LocalContext.current

    // Variables de estado para los campos de entrada y usuario logueado
    var aula by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var horaInicio by remember { mutableStateOf("") }
    var usuarioId by remember { mutableStateOf(0) }  // Inicializamos el ID del usuario como 0
    var usuarioLogueado by remember { mutableStateOf<Usuario?>(null) } // Usuario logueado

    // Variables de estado para la validación de errores
    var errorAula by remember { mutableStateOf(false) }
    var errorFecha by remember { mutableStateOf(false) }
    var errorHoraInicio by remember { mutableStateOf(false) }


    // Cargar el usuarioId desde la API (opcional, requiere ver con el profesor)
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
            .fillMaxSize() // La columna ocupa todo el espacio de la pantalla
            .padding(16.dp) // Espaciado de 16dp
    ) {
        // Barra superior con título y botón de navegación
        TopAppBar(
            title = { Text("Gestionar Reservas") },
            actions = {
                TextButton(onClick = { navController.navigate("menu") }) {
                    Text("Menú Principal", color = Color.White) // Botón para ir al menú principal
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = customRed, // Color de fondo de la barra superior
                titleContentColor = Color.White, // Color del título
                navigationIconContentColor = Color.White // Color de los íconos de navegación
            )
        )

        Spacer(modifier = Modifier.height(16.dp)) // Espaciado vertical de 16dp


        // Botones de navegación para listar y cancelar reservas
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { navController.navigate("listarReservas") }, // Navega a la pantalla de listar reservas
                colors = ButtonDefaults.buttonColors(containerColor = customRed),
                modifier = Modifier.weight(1f)
            ) {
                Text("Listar Reservas", color = MaterialTheme.colorScheme.onPrimary) // Texto del botón
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { navController.navigate("cancelarReservas") }, // Navega a la pantalla de cancelar reservas
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancelar Reservas", color = MaterialTheme.colorScheme.onSecondary) // Texto del botón
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tarjeta de formulario para registrar una nueva reserva
        Card(
            modifier = Modifier.fillMaxWidth(), // La tarjeta ocupa todo el ancho
            elevation = CardDefaults.cardElevation(8.dp) // Elevación de la tarjeta
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Registrar Nueva Reserva", style = MaterialTheme.typography.headlineSmall)


                // Campo de texto para el aula
                OutlinedTextField(
                    value = aula,
                    onValueChange = {
                        // Permite cambiar el valor si es un número dentro del rango 1-6 o vacío
                        if (it.isEmpty() || (it.all { char -> char.isDigit() } && it.toIntOrNull() in 1..6)) {
                            aula = it // Actualiza el valor de la variable 'aula' si es válido o vacío
                            errorAula = false // No hay error si el valor es válido
                        } else {
                            errorAula = true // Activa el mensaje de error si el valor no es válido
                        }
                    },
                    label = { Text("Aula (1-6)") }, // Etiqueta que aparece dentro del campo de entrada
                    modifier = Modifier.fillMaxWidth(), // El campo ocupa todo el ancho disponible
                    isError = errorAula, // Muestra el borde en rojo si hay error
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = customRed, // Color rojo para el borde si el campo está enfocado y con error
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) // Color del texto de la etiqueta cuando el campo no está enfocado
                    )
                )
                // Mensaje de error que se muestra si 'errorAula' es verdadero
                if (errorAula) {
                    Text("El aula debe ser un número entre 1 y 6", color = MaterialTheme.colorScheme.error)
                }


                // Campo de texto para la fecha
                OutlinedTextField(
                    value = fecha,
                    onValueChange = {
                        fecha = it // Actualiza el valor de la variable 'fecha'

                        // Solo se valida si la longitud de la entrada es 10 (DD/MM/YYYY) o si está vacío
                        errorFecha = if (fecha.length == 10) {
                            !fecha.matches(Regex("^([0-2][0-9]|3[01])/(0[1-9]|1[0-2])/(\\d{4})\$")) // Verifica si no coincide con el formato correcto
                        } else {
                            false // Mientras la longitud no sea 10, no muestra el error
                        }
                    },
                    label = { Text("Fecha (DD/MM/YYYY)") }, // Etiqueta que aparece dentro del campo de entrada
                    modifier = Modifier.fillMaxWidth(), // El campo ocupa todo el ancho disponible
                    isError = errorFecha, // Muestra el borde en rojo si hay error
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = customRed, // Color rojo para el borde si el campo está enfocado y con error
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) // Color del texto de la etiqueta cuando el campo no está enfocado
                    )
                )
                // Mensaje de error que se muestra si 'errorFecha' es verdadero
                if (errorFecha) {
                    Text("La fecha debe tener el formato DD/MM/YYYY", color = MaterialTheme.colorScheme.error)
                }


                // Campo de texto para la hora de inicio
                OutlinedTextField(
                    value = horaInicio,
                    onValueChange = {
                        horaInicio = it // Actualiza el valor de la variable 'horaInicio'

                        // Solo se valida si la longitud de la entrada es 5 (HH:MM) o si está vacío
                        errorHoraInicio = if (horaInicio.length == 5) {
                            !horaInicio.matches(Regex("^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]\$")) // Verifica si no coincide con el formato
                        } else {
                            false // Mientras la longitud no sea 5, no muestra el error
                        }
                    },
                    label = { Text("Horario (HH:MM)") }, // Etiqueta que aparece dentro del campo de entrada
                    modifier = Modifier.fillMaxWidth(), // El campo ocupa todo el ancho disponible
                    isError = errorHoraInicio, // Muestra el borde en rojo si hay error
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = customRed, // Color rojo para el borde si el campo está enfocado y con error
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) // Color del texto de la etiqueta cuando el campo no está enfocado
                    )
                )
                // Mensaje de error que se muestra si 'errorHoraInicio' es verdadero
                if (errorHoraInicio) {
                    Text("La hora debe tener el formato HH:MM", color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para registrar la reserva
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
                    enabled = aula.isNotEmpty() && fecha.isNotEmpty() && horaInicio.isNotEmpty() && !errorAula && !errorFecha && !errorHoraInicio, // Verifica que no haya errores
                    colors = ButtonDefaults.buttonColors(containerColor = customRed),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Registrar Reserva", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}
