@file:OptIn(ExperimentalMaterial3Api::class)


package com.example.reservasaulas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
data class Libro(
    val titulo: String,
    val autor: String,
    val anho: String,
    val genero: String,
    val isbn: String,
    val portadaUrl: String
)
// Libros Screen
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun LibrosScreen(navController: NavController, libros: MutableList<Libro>) {
    val customRed = Color(0xFF791414) // Color rojo
    var titulo by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var anho by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var isbn by remember { mutableStateOf("") }
    val generosDisponibles = listOf("Ficción", "No Ficción", "Fantasía", "Ciencia Ficción", "Biografía", "Historia", "Misterio", "Romance")
    var portadaUrl by remember { mutableStateOf("") }
    var errorTitulo by remember { mutableStateOf(false) }
    var errorAutor by remember { mutableStateOf(false) }
    var errorAnho by remember { mutableStateOf(false) }
    var errorIsbn by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Barra superior
        TopAppBar(
            title = { Text("Gestión de Libros") },
            actions = {
                Row {
                    TextButton(onClick = { navController.navigate("menu") }) {
                        Text("Menú Principal", color = customRed)
                    }
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
                onClick = { navController.navigate("listarLibros") },
                colors = ButtonDefaults.buttonColors(containerColor = customRed),
                modifier = Modifier.weight(1f)
            ) {
                Text("Listar Libros", color = MaterialTheme.colorScheme.onPrimary)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { navController.navigate("cancelarLibros") },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier.weight(1f)
            ) {
                Text("Eliminar Libros", color = MaterialTheme.colorScheme.onSecondary)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Formulario de ingreso de libros
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
                Text("Registrar Nuevo Libro", style = MaterialTheme.typography.headlineSmall)

                // Campo Título
                OutlinedTextField(
                    value = titulo,
                    onValueChange = {
                        titulo = it
                        errorTitulo = it.isEmpty()
                    },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = errorTitulo,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = customRed, // Borde rojo cuando está enfocado
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), // Borde gris sin foco
                        focusedLabelColor = customRed, // Texto rojo de la etiqueta al enfocar
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), // Texto gris de la etiqueta sin foco
                        errorBorderColor = MaterialTheme.colorScheme.error, // Opcional para bordes de error
                        errorLabelColor = MaterialTheme.colorScheme.error // Opcional para texto de error
                    )
                )
                if (errorTitulo) {
                    Text(
                        "El título no puede estar vacío",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // Campo Autor
                OutlinedTextField(
                    value = autor,
                    onValueChange = {
                        autor = it
                        errorAutor = it.isEmpty()
                    },
                    label = { Text("Autor") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = errorAutor,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = customRed, // Borde rojo cuando está enfocado
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), // Borde gris sin foco
                        focusedLabelColor = customRed, // Texto rojo de la etiqueta al enfocar
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), // Texto gris de la etiqueta sin foco
                        errorBorderColor = MaterialTheme.colorScheme.error, // Opcional para bordes de error
                        errorLabelColor = MaterialTheme.colorScheme.error // Opcional para texto de error
                    )
                )
                if (errorAutor) {
                    Text(
                        "El autor no puede estar vacío",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // Campo Año
                OutlinedTextField(
                    value = anho,
                    onValueChange = {
                        anho = it
                        errorAnho = it.isEmpty() || it.toIntOrNull() == null || it.toInt() <= 0
                    },
                    label = { Text("Año") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = errorAnho,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = customRed, // Borde rojo cuando está enfocado
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), // Borde gris sin foco
                        focusedLabelColor = customRed, // Texto rojo de la etiqueta al enfocar
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), // Texto gris de la etiqueta sin foco
                        errorBorderColor = MaterialTheme.colorScheme.error, // Opcional para bordes de error
                        errorLabelColor = MaterialTheme.colorScheme.error // Opcional para texto de error
                    )
                )
                if (errorAnho) {
                    Text(
                        "El año debe ser un número positivo",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // Campo Género (Desplegable)
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = genero,
                        onValueChange = { genero = it },
                        label = { Text("Género") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        readOnly = true,
                        trailingIcon = {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = customRed, // Borde rojo cuando está enfocado
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), // Borde gris sin foco
                            focusedLabelColor = customRed, // Texto rojo de la etiqueta al enfocar
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), // Texto gris de la etiqueta sin foco
                            errorBorderColor = MaterialTheme.colorScheme.error, // Opcional para bordes de error
                            errorLabelColor = MaterialTheme.colorScheme.error // Opcional para texto de error
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        generosDisponibles.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    genero = opcion
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // Campo ISBN
                OutlinedTextField(
                    value = isbn,
                    onValueChange = {
                        isbn = it
                        errorIsbn = it.length != 13 || it.toLongOrNull() == null
                    },
                    label = { Text("ISBN") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = errorIsbn,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = customRed, // Borde rojo cuando está enfocado
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), // Borde gris sin foco
                        focusedLabelColor = customRed, // Texto rojo de la etiqueta al enfocar
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), // Texto gris de la etiqueta sin foco
                        errorBorderColor = MaterialTheme.colorScheme.error, // Opcional para bordes de error
                        errorLabelColor = MaterialTheme.colorScheme.error // Opcional para texto de error
                    ),
                    placeholder = { Text("Debe tener 13 dígitos") }
                )
                if (errorIsbn) {
                    Text(
                        "El ISBN debe ser un número de 13 dígitos",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // Campo Portada (URL)
                OutlinedTextField(
                    value = portadaUrl,
                    onValueChange = { portadaUrl = it },
                    label = { Text("Portada (URL)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = customRed, // Borde rojo cuando está enfocado
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), // Borde gris sin foco
                        focusedLabelColor = customRed, // Texto rojo de la etiqueta al enfocar
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), // Texto gris de la etiqueta sin foco
                        errorBorderColor = MaterialTheme.colorScheme.error, // Opcional para bordes de error
                        errorLabelColor = MaterialTheme.colorScheme.error // Opcional para texto de error
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón Registrar Libro
                Button(
                    onClick = {
                        libros.add(Libro(titulo, autor, anho, genero, isbn, portadaUrl))
                        titulo = ""
                        autor = ""
                        anho = ""
                        genero = ""
                        isbn = ""
                        portadaUrl = ""
                    },
                    enabled = titulo.isNotEmpty() && autor.isNotEmpty() && anho.isNotEmpty() &&
                            genero.isNotEmpty() && isbn.isNotEmpty() && portadaUrl.isNotEmpty() &&
                            !errorTitulo && !errorAutor && !errorAnho && !errorIsbn,
                    colors = ButtonDefaults.buttonColors(containerColor = customRed),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Registrar Libro", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}
