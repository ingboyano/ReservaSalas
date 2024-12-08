package com.example.reservasaulas

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ReservaApi {
    @GET("/reservas")
    fun getAllReservas(): Call<List<Reserva>>

    @POST("/reservas")
    fun createReserva(@Body reserva: Reserva): Call<Reserva>
}