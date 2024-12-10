package com.example.reservasaulas

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class Reserva(
    val aula: String,
    val fecha: String,
    val horario: String,
    val usuarioId: Int
)

interface ReservaApi {
    @GET("/reservas")
    fun getAllReservas(): Call<List<Reserva>>

    @POST("/reservas")
    fun createReserva(@Body reserva: Reserva): Call<Reserva>
}