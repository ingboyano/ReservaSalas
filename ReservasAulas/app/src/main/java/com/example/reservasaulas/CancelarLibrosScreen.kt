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

// Screen to cancel/delete books
@Composable
fun CancelarLibrosScreen(navController: NavController, libros: List<Libro>) {
    val libros = remember { mutableStateListOf<Libro>() }
    val errorMessage = remember { mutableStateOf("") } // Cambiado a mutableStateOf
    val isLoading = remember { mutableStateOf(true) }

    // Llamada a la API para obtener los libros
    LaunchedEffect(Unit) {
        val api = RetrofitClient.retrofit.create(LibroApi::class.java)
        api.getAllLibros().enqueue(object : Callback<List<Libro>> {
            override fun onResponse(call: Call<List<Libro>>, response: Response<List<Libro>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        libros.clear()
                        libros.addAll(it)
                    }
                    isLoading.value = false
                } else {
                    // Si la respuesta no es exitosa
                    errorMessage.value = "Error al cargar los libros: ${response.message()}"
                    isLoading.value = false
                }
            }

            override fun onFailure(call: Call<List<Libro>>, t: Throwable) {
                // Manejo de error en la llamada
                errorMessage.value = "Error de red: ${t.localizedMessage}"
                isLoading.value = false
            }
        })
    }

    val customRed = Color(0xFF791414)
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TopAppBar(
            title = { Text("Eliminar Libros") },
            navigationIcon = {
                IconButton(onClick = { navController.navigate("libros") }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver a Registro de Libros")
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
            Text("Cargando libros...", style = MaterialTheme.typography.bodyLarge)
        } else {
            // Mostrar libros si hay datos disponibles
            if (libros.isEmpty()) {
                Text("No hay libros para eliminar.", style = MaterialTheme.typography.bodyLarge)
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(libros) { libro ->
                        // Aquí agregamos el fondo blanco debajo de cada libro
                        Card (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White) // Fondo blanco
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Título: ${libro.titulo}", style = MaterialTheme.typography.bodyLarge)
                                    Text("Autor: ${libro.autor}", style = MaterialTheme.typography.bodyMedium)
                                }
                            Button(
                                onClick = {
                                    // Eliminar libro de la API y de la lista local
                                    val api = RetrofitClient.retrofit.create(LibroApi::class.java)
                                    api.deleteLibro(libro.isbn).enqueue(object : Callback<Void> {
                                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                            if (response.isSuccessful) {
                                                libros.remove(libro) // Eliminar libro de la lista local
                                            } else {
                                                // Error al eliminar el libro
                                                errorMessage.value = "No se pudo eliminar el libro: ${response.message()}"
                                            }
                                        }

                                        override fun onFailure(call: Call<Void>, t: Throwable) {
                                            // Manejo de error al eliminar el libro
                                            errorMessage.value = "Error de red al eliminar el libro: ${t.localizedMessage}"
                                        }
                                    })
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                                modifier = Modifier.padding(start = 8.dp)
                            ) {
                                Text("Eliminar", color = MaterialTheme.colorScheme.onError)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
