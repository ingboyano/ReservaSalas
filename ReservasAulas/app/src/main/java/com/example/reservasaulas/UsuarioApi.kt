package com.example.reservasaulas

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// Clase de solicitud de login
data class LoginRequest(
    val nombreUsuario: String,
    val contrasenha: String
)

// Interface para las APIs
interface UsuarioApi {
    @POST("/auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<ResponseBody>

    @GET("/usuarios") // Endpoint para obtener todos los usuarios
    fun getAllUsuarios(): Call<List<Usuario>> // Obtener lista de usuarios

    @GET("/usuarios/{idUsuario}") // Endpoint para obtener un usuario específico
    fun getUsuarioById(@Path("idUsuario") idUsuario: Int): Call<Usuario>
}

// Clase de datos de un usuario
data class Usuario(val idUsuario: Int, val nombreUsuario: String, val contrasenha: String)