@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.reservasaulas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.reservasaulas.ui.theme.ReservasAulasTheme

// MainActivity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReservasAulasTheme {
                MainApp()
            }
        }
    }
}

// MainApp with Navigation
@Composable
fun MainApp() {

    val navController = rememberNavController()
    var loggedIn by remember { mutableStateOf(false) }
    val reservas = remember { mutableStateListOf<Reserva>() }
    val libros = remember { mutableStateListOf<Libro>() }

    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.arbol),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.5f) // Transparencia
        )

        // Si no está logueado, mostramos el LoginScreen con imagen sobre el fondo
        if (!loggedIn) {
            // Imagen sobre el fondo en el login
            Image(
                painter = painterResource(id = R.drawable.logouaa), // Cambié "logo" por "logouaa"
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopCenter) // Posiciona la imagen en la parte superior
                    .padding(top = 64.dp) // Ajuste el padding superior si es necesario
            )

            // Pantalla de Login
            LoginScreen { loggedIn = true }
        } else {
            // Si está logueado, navegamos a la siguiente pantalla
            NavHost(navController = navController, startDestination = "menu") {
                composable("menu") {
                    MenuPrincipalScreen(navController, onLogout = { loggedIn = false })
                }
                composable("reservas") {
                    ReservasScreen(navController = navController, reservas = reservas)
                }
                composable("listarReservas") {
                    ListarReservasScreen(navController = navController, reservas = reservas)
                }
                composable("cancelarReservas") {
                    CancelarReservasScreen(navController, reservas)
                }
                composable("libros") {
                    LibrosScreen(navController, libros)
                }
                composable("listarLibros") {
                    ListarLibrosScreen(navController, libros)
                }
                composable("cancelarLibros") {
                    CancelarLibrosScreen(navController = navController, libros = libros)
                }
            }
        }
    }
}




