@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.reservasaulas

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuBoxScope
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.reservasaulas.ui.theme.ReservasAulasTheme

// Data classes
data class Reserva(val aula: String, val fecha: String, val horario: String)


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
                    ReservaScreen(navController, reservas, onLogout = { loggedIn = false })
                }
                composable("listarReservas") {
                    ListarReservasScreen(navController, reservas)
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
                    CancelarLibrosScreen(navController, libros)
                }
            }
        }
    }
}




