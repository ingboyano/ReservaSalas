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

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {

    val customRed = Color(0xFF791414) // Código hexadecimal para rojo

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
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 32.sp, // Ajusta el tamaño de la fuente
                        fontWeight = FontWeight.Bold // Pone el texto en negrita
                    ),
                    color = customRed,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .padding(top = 16.dp)
                )

                // Campo de Usuario
                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        username = it
                        errorMessage = "" // Limpia el mensaje de error al escribir
                    },
                    label = {
                        Text("Usuario", color = if (errorMessage.isNotEmpty()) customRed else MaterialTheme.colorScheme.onBackground)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Usuario"
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Introduce tu usuario", color = MaterialTheme.colorScheme.onSurface) }, // Texto dentro del campo en color negro
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = customRed, // Borde rojo cuando está enfocado
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), // Borde gris sin foco
                        focusedLabelColor = customRed, // Texto rojo de la etiqueta al enfocar
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), // Texto gris de la etiqueta sin foco
                        errorBorderColor = MaterialTheme.colorScheme.error, // Bordes de error
                        errorLabelColor = MaterialTheme.colorScheme.error // Texto de error
                    )
                )

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

// Campo de Contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = "" // Limpia el mensaje de error al escribir
                    },
                    label = {
                        Text("Contraseña", color = if (errorMessage.isNotEmpty()) customRed else MaterialTheme.colorScheme.onBackground)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Contraseña"
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Introduce tu contraseña", color = MaterialTheme.colorScheme.onSurface) }, // Texto dentro del campo en color negro
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = customRed, // Borde rojo cuando está enfocado
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), // Borde gris sin foco
                        focusedLabelColor = customRed, // Texto rojo de la etiqueta al enfocar
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), // Texto gris de la etiqueta sin foco
                        errorBorderColor = MaterialTheme.colorScheme.error, // Bordes de error
                        errorLabelColor = MaterialTheme.colorScheme.error // Texto de error
                    )
                )

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

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
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF791414),
                        contentColor = Color.White),
                    shape = MaterialTheme.shapes.medium
                )

                {
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

    val customRed = Color(0xFF791414) // Código hexadecimal para rojo


    Box(
        modifier = Modifier
            .fillMaxSize(), // Ocupa toda la pantalla
        contentAlignment = Alignment.Center // Centra el contenido
    ) {
        // Contenedor principal
        Column(
            modifier = Modifier
                .width(350.dp) // Establece un ancho específico
                .height(500.dp) // Establece una altura específica
                .background(
                    color = Color.White, // Fondo blanco
                    shape = MaterialTheme.shapes.medium // Esquinas redondeadas
                )
                .padding(16.dp), // Espaciado interno
            verticalArrangement = Arrangement.Center, // Contenido centrado verticalmente
            horizontalAlignment = Alignment.CenterHorizontally // Contenido centrado horizontalmente
        ) {
            // Título
            Text(
                text = "¡Bienvenido a BiblioUAA!",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), // Negrita
                color = customRed,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 100.dp)
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
                        text = "Reserva de Salas",
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
                    .padding(top = 50.dp)
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
}


// Reserva Screens
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




@Composable
fun ListarReservasScreen(navController: NavController, reservas: List<Reserva>) {
    val customRed = Color(0xFF791414)
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TopAppBar(
            title = { Text("Listar Reservas") },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = customRed,
                titleContentColor = Color.White, // Letras del título en blanco
                navigationIconContentColor = Color.White)
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
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column {
                            Column(modifier = Modifier.padding(16.dp)) {
                            Text("Aula: ${reserva.aula}")
                            Text("Fecha: ${reserva.fecha}")
                            Text("Horario: ${reserva.horario}")
                        }
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

// Función de listar libros
@Composable
fun ListarLibrosScreen(navController: NavController, libros: List<Libro>) {
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
                titleContentColor = Color.White, // Letras del título en blanco
                navigationIconContentColor = Color.White)
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
