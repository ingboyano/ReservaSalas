package com.example.reservasaulas

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LibroApi {
    @GET("/libros")
    fun getAllLibros(): Call<List<Libro>>

    @POST("/libros")
    fun createLibro(@Body libro: Libro): Call<Libro>
}
