package hu.bme.aut.rentapp.data

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object ServiceGenerator {

    val client = OkHttpClient.Builder().build()

    var gson = GsonBuilder()
        .setLenient()
        .create()

    fun <T> buildService(service: Class<T>, baseURL: String = "http://192.168.1.8:8085/"): T {
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(client)
            .build().create(service)
    }

}