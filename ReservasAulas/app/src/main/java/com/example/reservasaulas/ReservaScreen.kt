package com.example.reservasaulas

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// Reserva Screens
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservaScreen(navController: NavController, reservas: MutableList<Reserva>, onLogout: () -> Unit) {
    val customRed = Color(0xFF791414) // Color rojo
    var aula by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var horario by remember { mutableStateOf("") }
    var errorAula by remember { mutableStateOf(false) }
    var errorHorario by remember { mutableStateOf(false) }
    var errorFecha by remember { mutableStateOf(false) }
    var timePickerDialogVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Barra superior
        TopAppBar(
            title = { Text("Reserva de Salas") },
            actions = {
                TextButton(onClick = { navController.navigate("menu") }) {
                    Text("Menú Principal", color = customRed)
                }
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { navController.navigate("listarReservas") },
                colors = ButtonDefaults.buttonColors(containerColor = customRed), // Cambiado a customRed
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

        // Formulario de ingreso de reservas
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


                // Campo de Aula
                OutlinedTextField(
                    value = aula,
                    onValueChange = {
                        aula = it
                        errorAula = !validarNumeroAula(it)
                    },
                    label = {
                        Text("Sala (1-10)", color = if (errorAula) customRed else MaterialTheme.colorScheme.onBackground)
                    },
                    isError = errorAula,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = { Text("Ej: 5", color = MaterialTheme.colorScheme.onSurface) }, // Texto dentro del campo en color negro
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = customRed, // Borde rojo cuando está enfocado
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), // Borde gris sin foco
                        focusedLabelColor = customRed, // Texto rojo de la etiqueta al enfocar
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), // Texto gris de la etiqueta sin foco
                        errorBorderColor = MaterialTheme.colorScheme.error, // Bordes de error
                        errorLabelColor = MaterialTheme.colorScheme.error // Texto de error
                    )
                )

                if (errorAula) {
                    Text(
                        text = "El número de sala debe estar entre 1 y 10.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

// Campo Fecha
                OutlinedTextField(
                    value = fecha,
                    onValueChange = {
                        fecha = it
                        errorFecha = !validarFormatoFecha(it)
                    },
                    label = {
                        Text("Fecha (dd/mm/aaaa)", color = if (errorFecha) customRed else MaterialTheme.colorScheme.onBackground)
                    },
                    isError = errorFecha,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("dd/mm/aaaa", color = MaterialTheme.colorScheme.onSurface) }, // Texto dentro del campo en color negro
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = customRed, // Borde rojo cuando está enfocado
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), // Borde gris sin foco
                        focusedLabelColor = customRed, // Texto rojo de la etiqueta al enfocar
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), // Texto gris de la etiqueta sin foco
                        errorBorderColor = MaterialTheme.colorScheme.error, // Bordes de error
                        errorLabelColor = MaterialTheme.colorScheme.error // Texto de error
                    )
                )

                if (errorFecha) {
                    Text(
                        "Formato de fecha inválido. Debe ser dd/mm/aaaa.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

// Campo Horario
                OutlinedTextField(
                    value = horario,
                    onValueChange = {
                        horario = it
                        errorHorario = !validarFormatoHora(it)
                    },
                    label = {
                        Text("Horario (hh:mm)", color = if (errorHorario) customRed else MaterialTheme.colorScheme.onBackground)
                    },
                    isError = errorHorario,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("hh:mm", color = MaterialTheme.colorScheme.onSurface) }, // Texto dentro del campo en color negro
                    trailingIcon = {
                        IconButton(onClick = { timePickerDialogVisible = true }) {
                            Icon(Icons.Default.Schedule, contentDescription = "Seleccionar Hora", tint = Color(0xFF791414))
                        }
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = customRed, // Borde rojo cuando está enfocado
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), // Borde gris sin foco
                        focusedLabelColor = customRed, // Texto rojo de la etiqueta al enfocar
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), // Texto gris de la etiqueta sin foco
                        errorBorderColor = MaterialTheme.colorScheme.error, // Bordes de error
                        errorLabelColor = MaterialTheme.colorScheme.error // Texto de error
                    )
                )

                if (errorHorario) {
                    Text(
                        "Formato de horario inválido. Debe ser hh:mm.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón Registrar
                Button(
                    onClick = {
                        if (!errorAula && !errorFecha && !errorHorario) {
                            reservas.add(Reserva(aula, fecha, horario))
                            aula = ""
                            fecha = ""
                            horario = ""
                        }
                    },
                    enabled = aula.isNotEmpty() && fecha.isNotEmpty() && horario.isNotEmpty() &&
                            !errorAula && !errorFecha && !errorHorario,
                    colors = ButtonDefaults.buttonColors(containerColor = customRed),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Registrar Reserva", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }

    // TimePickerDialog
    if (timePickerDialogVisible) {
        val context = LocalContext.current
        val timePickerDialog = remember {
            TimePickerDialog(
                context,
                { _, hourOfDay, minute ->
                    horario = "%02d:%02d".format(hourOfDay, minute)
                    timePickerDialogVisible = false
                },
                12, 0, true
            )
        }
        timePickerDialog.show()
    }
}

// Validar número de aula
fun validarNumeroAula(aula: String): Boolean {
    val numero = aula.toIntOrNull()
    return numero != null && numero in 1..10
}

// Validar formato de hora
fun validarFormatoHora(hora: String): Boolean {
    val regex = Regex("^([01]\\d|2[0-3]):([0-5]\\d)$")
    return regex.matches(hora)
}

// Validar formato de fecha
fun validarFormatoFecha(fecha: String): Boolean {
    val regex = Regex("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/(\\d{4})$")
    return regex.matches(fecha)
}



