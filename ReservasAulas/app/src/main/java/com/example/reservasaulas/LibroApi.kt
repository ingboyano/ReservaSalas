package com.example.reservasaulas

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// Modelo de datos Libro
data class Libro(
    val titulo: String,
    val autor: String,
    val anho: Int,
    val genero: String,
    val isbn: String,
    val portadaUrl: String
)

// Interfaz para la API de libros
interface LibroApi {
    @GET("/libros")
    fun getAllLibros(): Call<List<Libro>>

    @POST("/libros")
    fun createLibro(@Body libro: Libro): Call<Libro>

    @DELETE("/libros/{isbn}")
    fun deleteLibro(@Path("isbn") isbn: String): Call<Void> // Elimina un libro por su ISBN
}