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
import retrofit2.Retrofit


// Función de listar libros
@Composable
fun ListarLibrosScreen(navController: NavController, libros: List<Libro>) {
    val context = LocalContext.current
    val libros = remember { mutableStateListOf<Libro>() }
    val isLoading = remember { mutableStateOf(true) }
    val errorMessage = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(true) {
        // Realizamos la llamada para obtener los libros
        RetrofitClient.libroApi.getAllLibros().enqueue(object : retrofit2.Callback<List<Libro>> {
            override fun onResponse(call: Call<List<Libro>>, response: retrofit2.Response<List<Libro>>) {
                if (response.isSuccessful) {
                    // Si la respuesta es exitosa, actualizamos la lista de libros
                    libros.clear()
                    libros.addAll(response.body() ?: emptyList())
                    isLoading.value = false
                } else {
                    // Si la respuesta no es exitosa, mostramos un mensaje de error
                    isLoading.value = false
                    errorMessage.value = "Error al cargar los libros"
                }
            }

            override fun onFailure(call: Call<List<Libro>>, t: Throwable) {
                // Si ocurre un error durante la solicitud, mostramos un mensaje
                isLoading.value = false
                errorMessage.value = "Error de red: ${t.message}"
            }
        })
    }

    val customRed = Color(0xFF791414)
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TopAppBar(
            title = { Text("Listar Libros") },
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

        // Muestra un mensaje de carga
        if (isLoading.value) {
            Text("Cargando libros...", style = MaterialTheme.typography.bodyLarge)
        }

        // Muestra un mensaje de error si falla la carga
        if (errorMessage.value != null) {
            Text("Error: ${errorMessage.value}", style = MaterialTheme.typography.bodyLarge, color = Color.Red)
        }

        // Si no hay libros, muestra mensaje
        if (libros.isEmpty() && !isLoading.value) {
            Text("No hay libros registrados.", style = MaterialTheme.typography.bodyLarge)
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(libros) { libro ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Título: ${libro.titulo}", style = MaterialTheme.typography.bodyLarge)
                            Text("Autor: ${libro.autor}", style = MaterialTheme.typography.bodyMedium)
                            Text("Año: ${libro.anho}", style = MaterialTheme.typography.bodyMedium)
                            Text("Género: ${libro.genero}", style = MaterialTheme.typography.bodyMedium)
                            Text("ISBN: ${libro.isbn}", style = MaterialTheme.typography.bodyMedium)
                            Image(
                                painter = rememberAsyncImagePainter(model = libro.portadaUrl),
                                contentDescription = "Portada del libro",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        }
    }
}
