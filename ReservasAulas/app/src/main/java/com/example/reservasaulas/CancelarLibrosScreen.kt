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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CancelarLibrosScreen(navController: NavController, libros: MutableList<Libro>) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TopAppBar(
            title = { Text("Eliminar Libros") },
            navigationIcon = {
                IconButton(onClick = { navController.navigate("libros") }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver a Registro de Libros")
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (libros.isEmpty()) {
            Text("No hay libros para eliminar.", style = MaterialTheme.typography.bodyLarge)
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(libros) { libro ->
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
                            onClick = { libros.remove(libro) },
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
