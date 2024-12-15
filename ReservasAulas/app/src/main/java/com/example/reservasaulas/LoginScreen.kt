package com.example.reservasaulas

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

@OptIn(ExperimentalMaterial3Api::class)
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
                    placeholder = { Text("Introduce tu contraseña", color = MaterialTheme.colorScheme.onSurface) },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = customRed,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        focusedLabelColor = customRed,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        errorBorderColor = MaterialTheme.colorScheme.error,
                        errorLabelColor = MaterialTheme.colorScheme.error
                    )
                )

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Button(
                    onClick = {
                        // Verificar los valores de usuario y contraseña antes de hacer la solicitud
                        Log.d("LoginTest", "Usuario: $username, Contraseña: $password")
                        if (username.isEmpty() || password.isEmpty()) {
                            errorMessage = "Por favor, ingrese usuario y contraseña"
                        } else {
                            // Llamar a la API de login
                            val loginRequest = LoginRequest(username, password)
                            RetrofitClient.usuarioApi.login(loginRequest).enqueue(object : Callback<ResponseBody> {
                                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                    if (response.isSuccessful) {
                                        val responseBody = response.body()?.string() ?: "Respuesta vacía"
                                        Log.d("LoginResponse", "Código: ${response.code()}, Mensaje: ${response.message()}, Cuerpo: $responseBody")

                                        if (responseBody == "Login exitoso") {
                                            Log.d("Login", "Login exitoso")
                                            onLoginSuccess() // Lógica de éxito
                                        } else {
                                            Log.e("LoginError", "Respuesta inesperada: $responseBody")
                                            errorMessage = "Usuario o contraseña incorrectos"
                                        }
                                    } else {
                                        Log.e("LoginError", "Código de respuesta: ${response.code()} - Mensaje: ${response.message()}")
                                        errorMessage = "Usuario o contraseña incorrectos"
                                    }
                                }
                                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                    Log.e("LoginFailure", "Error de conexión: ${t.message}")
                                    errorMessage = "Error de conexión: ${t.message}"
                                }
                            })
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF791414),
                        contentColor = Color.White
                    ),
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
