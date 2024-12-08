import com.example.reservasaulas.LibroApi
import com.example.reservasaulas.ReservaApi
import com.example.reservasaulas.UsuarioApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// Clase Singleton para configurar y obtener instancias de Retrofit
object RetrofitClient {
    private const val BASE_URL = "http://192.168.100.96:8080" // URL base de la API

    // Crear un cliente OkHttp con tiempo de espera personalizado
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)  // Timeout de conexión
        .readTimeout(10, TimeUnit.SECONDS)     // Timeout de lectura
        .writeTimeout(10, TimeUnit.SECONDS)    // Timeout de escritura
        .build()

    // Instanciar Retrofit con el cliente configurado
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)  // Usar el cliente con los timeouts
            .addConverterFactory(GsonConverterFactory.create())  // Usar conversor Gson
            .build()
    }




    // Crear instancias de las APIs
    val usuarioApi: UsuarioApi by lazy {
        retrofit.create(UsuarioApi::class.java)  // Instancia de UsuarioApi
    }

    val libroApi: LibroApi by lazy {
        retrofit.create(LibroApi::class.java)  // Instancia de LibroApi (suponiendo que la clase esté definida)
    }

    val reservaApi: ReservaApi by lazy {
        retrofit.create(ReservaApi::class.java)  // Instancia de ReservaApi (suponiendo que la clase esté definida)
    }
}



