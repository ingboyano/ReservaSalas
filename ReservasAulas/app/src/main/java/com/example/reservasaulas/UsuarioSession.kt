package com.example.reservasaulas


import android.content.Context

// Clase para gestionar la sesión del usuario
object UsuarioSession {

    private const val PREF_NAME = "usuario_data"
    private const val KEY_ID_USUARIO = "idUsuario"

    // Guardar el idUsuario en SharedPreferences
    fun guardarIdUsuario(context: Context, idUsuario: Int) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_ID_USUARIO, idUsuario)
        editor.apply()  // Guardar cambios
    }

    // Obtener el idUsuario desde SharedPreferences
    fun obtenerIdUsuario(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(KEY_ID_USUARIO, -1)  // Devuelve -1 si no se encuentra el idUsuario
    }
}