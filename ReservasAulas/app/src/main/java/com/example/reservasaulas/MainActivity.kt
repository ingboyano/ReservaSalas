@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.reservasaulas

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Book
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.reservasaulas.ui.theme.ReservasAulasTheme

// Data classes
data class Reserva(val aula: String, val fecha: String, val horario: String)
data class Libro(
    val titulo: String,
    val autor: String,
    val anho: String,
    val genero: String,
    val isbn: String,
    val portadaUrl: String
)

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

    if (loggedIn) {
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
    } else {
        LoginScreen { loggedIn = true }
    }
}

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") } // Para mostrar mensajes de error

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título
                Text(
                    text = "Inicio de Sesión", //INICIO SESION
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Campo de Usuario
                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        username = it
                        errorMessage = "" // Limpia el mensaje de error al escribir
                    },
                    label = { Text("Usuario") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Usuario"
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo de Contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = "" // Limpia el mensaje de error al escribir
                    },
                    label = { Text("Contraseña") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Contraseña"
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                // Mostrar mensaje de error si hay
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                // Botón de Ingresar
                Button(
                    onClick = {
                        if (username.isEmpty() || password.isEmpty()) {
                            errorMessage = "Por favor, ingrese usuario y contraseña"
                        } else {
                            onLoginSuccess()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = "Iniciar Sesión",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}



// Menu Principal
@Composable
fun MenuPrincipalScreen(navController: NavController, onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.secondary,
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título
        Text(
            text = "¡Bienvenido a BiblioUAA!",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Botón Gestión de Reservas
        Button(
            onClick = { navController.navigate("reservas") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = MaterialTheme.shapes.medium
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Icono Reservas",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Reserva de salas",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        // Botón Gestión de Libros
        Button(
            onClick = { navController.navigate("libros") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
            shape = MaterialTheme.shapes.medium
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.Book,
                    contentDescription = "Icono Libros",
                    tint = MaterialTheme.colorScheme.onSecondary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Gestiona tus Libros",
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón Cerrar Sesión
        Button(
            onClick = { onLogout() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = "Cerrar Sesión",
                color = MaterialTheme.colorScheme.onError
            )
        }
    }
}


// Reserva Screens
@Composable
fun ReservaScreen(navController: NavController, reservas: MutableList<Reserva>, onLogout: () -> Unit) {
    var aula by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var horario by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Barra superior
        TopAppBar(
            title = { Text("Reservas de Salas") },
            actions = {
                Row {
                    TextButton(onClick = { navController.navigate("menu") }) {
                        Text("Menú Principal")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = onLogout) {
                        Text("Cerrar Sesión")
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
                onClick = { navController.navigate("listarReservas") },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
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
            var errorFecha by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Registrar Nueva Reserva", style = MaterialTheme.typography.headlineSmall)
                OutlinedTextField(
                    value = aula,
                    onValueChange = { aula = it },
                    label = { Text("Sala") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = fecha,
                    onValueChange = {
                        fecha = it
                        errorFecha = !validarFormatoFecha(it)
                    },
                    label = { Text("Fecha (dd/mm/aaaa)") },
                    isError = errorFecha,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("dd/mm/aaaa") },
                )
                if (errorFecha) {
                    Text(
                        "Formato de fecha inválido. Debe ser dd/mm/aaaa.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                OutlinedTextField(
                    value = horario,
                    onValueChange = { horario = it },
                    label = { Text("Horario") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (aula.isNotEmpty() && fecha.isNotEmpty() && horario.isNotEmpty() && !errorFecha) {
                            reservas.add(Reserva(aula, fecha, horario))
                            aula = ""
                            fecha = ""
                            horario = ""
                        }
                    },
                    enabled = aula.isNotEmpty() && fecha.isNotEmpty() && horario.isNotEmpty() && !errorFecha, // Botón habilitado solo si todo es válido
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Registrar Reserva", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }

    }
}
// Función para validar la fecha
fun validarFormatoFecha(fecha: String): Boolean {
    val regex = Regex("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/(\\d{4})$")
    return regex.matches(fecha)
}

@Composable
fun ListarReservasScreen(navController: NavController, reservas: List<Reserva>) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TopAppBar(
            title = { Text("Listar Reservas") },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (reservas.isEmpty()) {
            Text("No hay reservas registradas.", style = MaterialTheme.typography.bodyLarge)
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(reservas) { reserva ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Aula: ${reserva.aula}")
                            Text("Fecha: ${reserva.fecha}")
                            Text("Horario: ${reserva.horario}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CancelarReservasScreen(navController: NavController, reservas: MutableList<Reserva>) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TopAppBar(
            title = { Text("Cancelar Reservas") },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (reservas.isEmpty()) {
            Text("No hay reservas para cancelar.", style = MaterialTheme.typography.bodyLarge)
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(reservas) { reserva ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Aula: ${reserva.aula}")
                            Text("Fecha: ${reserva.fecha}")
                            Text("Horario: ${reserva.horario}")
                        }
                        Button(
                            onClick = { reservas.remove(reserva) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Eliminar", color = MaterialTheme.colorScheme.onError)
                        }
                    }
                }
            }
        }
    }
}

// Libros Screen
@Composable
fun LibrosScreen(navController: NavController, libros: MutableList<Libro>) {
    var titulo by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var anho by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var isbn by remember { mutableStateOf("") }
    var portadaUrl by remember { mutableStateOf("") }

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
                        Text("Menú Principal")
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
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
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
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = autor,
                    onValueChange = { autor = it },
                    label = { Text("Autor") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = anho,
                    onValueChange = { anho = it },
                    label = { Text("Año") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = genero,
                    onValueChange = { genero = it },
                    label = { Text("Género") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = isbn,
                    onValueChange = { isbn = it },
                    label = { Text("ISBN") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = portadaUrl,
                    onValueChange = { portadaUrl = it },
                    label = { Text("Portada (URL)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (titulo.isNotEmpty() && autor.isNotEmpty() && anho.isNotEmpty() && genero.isNotEmpty() && isbn.isNotEmpty() && portadaUrl.isNotEmpty()) {
                            libros.add(Libro(titulo, autor, anho, genero, isbn, portadaUrl))
                            titulo = ""
                            autor = ""
                            anho = ""
                            genero = ""
                            isbn = ""
                            portadaUrl = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Registrar Libro", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}

// Función de listar libros
@Composable
fun ListarLibrosScreen(navController: NavController, libros: List<Libro>) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TopAppBar(
            title = { Text("Listar Libros") },
            navigationIcon = {
                IconButton(onClick = { navController.navigate("libros") }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver a Registro de Libros")
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (libros.isEmpty()) {
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
