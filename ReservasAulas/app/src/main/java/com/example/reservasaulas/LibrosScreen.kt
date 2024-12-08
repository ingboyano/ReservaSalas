@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.reservasaulas

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response

@Composable
fun LibrosScreen(navController: NavController, libros: MutableList<Libro>) {
    val customRed = Color(0xFF791414) // Color rojo
    val context = LocalContext.current // Contexto para Toast
    var titulo by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var anho by remember { mutableStateOf(0) }
    var genero by remember { mutableStateOf("") }
    var isbn by remember { mutableStateOf("") }
    var portadaUrl by remember { mutableStateOf("") }
    var errorTitulo by remember { mutableStateOf(false) }
    var errorAutor by remember { mutableStateOf(false) }
    var errorAnho by remember { mutableStateOf(false) }
    var errorIsbn by remember { mutableStateOf(false) }
    var errorPortadaUrl by remember { mutableStateOf(false) } // Nuevo estado para la URL
    var expanded by remember { mutableStateOf(false) }
    val generosDisponibles = listOf(
        "Ficción", "No Ficción", "Fantasía", "Ciencia Ficción",
        "Biografía", "Historia", "Misterio", "Romance"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopAppBar(
            title = { Text("Gestión de Libros") },
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

                // Título
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
                        focusedBorderColor = customRed,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                )
                if (errorTitulo) {
                    Text("El título no puede estar vacío", color = MaterialTheme.colorScheme.error)
                }

                // Autor
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
                        focusedBorderColor = customRed,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                )
                if (errorAutor) {
                    Text("El autor no puede estar vacío", color = MaterialTheme.colorScheme.error)
                }

                // Año
                OutlinedTextField(
                    value = anho.toString(), // Convertimos el valor de anho a String
                    onValueChange = {
                        // Intentamos convertir el texto a entero
                        anho = it.toIntOrNull() ?: 0 // Si no es un número válido, asignamos 0
                        errorAnho = anho <= 0 // Validamos que el año sea mayor a 0
                    },
                    label = { Text("Año") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = errorAnho,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = customRed,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                )
                if (errorAnho) {
                    Text("Debe ser un número positivo", color = MaterialTheme.colorScheme.error)
                }

                // Género
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = genero,
                        onValueChange = { genero = it },
                        label = { Text("Género") },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            focusedBorderColor = customRed),
                        readOnly = true,
                        trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) }
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

                // ISBN
                OutlinedTextField(
                    value = isbn,
                    onValueChange = {
                        isbn = it
                        errorIsbn = it.length != 13 || it.toLongOrNull() == null
                    },
                    label = { Text("ISBN") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        focusedBorderColor = customRed),
                    isError = errorIsbn
                )
                if (errorIsbn) {
                    Text("El ISBN debe tener 13 dígitos", color = MaterialTheme.colorScheme.error)
                }

                // URL de portada
                OutlinedTextField(
                    value = portadaUrl,
                    onValueChange = {
                        portadaUrl = it
                        errorPortadaUrl = it.isEmpty() || !it.startsWith("http") // Validar URL
                    },
                    label = { Text("URL de portada") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        focusedBorderColor = customRed),
                    isError = errorPortadaUrl
                )
                if (errorPortadaUrl) {
                    Text("La URL debe ser válida", color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón Registrar
                Button(
                    onClick = {
                        val libroNuevo = Libro(titulo, autor, anho, genero, isbn, portadaUrl)

                        RetrofitClient.libroApi.createLibro(libroNuevo).enqueue(object : retrofit2.Callback<Libro> {
                            override fun onResponse(call: retrofit2.Call<Libro>, response: retrofit2.Response<Libro>) {
                                if (response.isSuccessful) {
                                    // Limpiar campos si la respuesta es exitosa
                                    titulo = ""
                                    autor = ""
                                    anho = 0
                                    genero = ""
                                    isbn = ""
                                    portadaUrl = ""
                                    Toast.makeText(context, "Libro registrado con éxito", Toast.LENGTH_SHORT).show()
                                } else {
                                    // Manejar error en la respuesta de la API
                                    Log.e("API_ERROR", "Error: ${response.errorBody()?.string()}")
                                }
                            }

                            override fun onFailure(call: retrofit2.Call<Libro>, t: Throwable) {
                                // Manejar error en la conexión
                                Log.e("API_ERROR", "Fallo en la conexión: ${t.message}")
                                Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show()
                            }
                        })
                    },
                    enabled = titulo.isNotEmpty() && autor.isNotEmpty() && anho > 0 && genero.isNotEmpty() &&
                            isbn.isNotEmpty() && portadaUrl.isNotEmpty() && !errorTitulo && !errorAutor && !errorAnho && !errorIsbn && !errorPortadaUrl,
                    colors = ButtonDefaults.buttonColors(containerColor = customRed),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Registrar Libro", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}

