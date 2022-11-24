package hu.bme.aut.rentapp

import hu.bme.aut.rentapp.models.LoginModel
import hu.bme.aut.rentapp.models.PostModel
import hu.bme.aut.rentapp.models.RegisterModel
import hu.bme.aut.rentapp.models.VehicleModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {


    @GET("/posts")
    fun getPosts(): Call<MutableList<PostModel>>

    @GET("/welcome")
    fun getWelcome(): Call<ResponseBody>

    @Headers("Content-type: application/json")
    @POST("/login")
    fun postLogin(@Body loginModel: LoginModel): Call<ResponseBody>

    @Headers("Content-type: application/json")
    @POST("/register")
    fun postRegister(@Body registerModel: RegisterModel): Call<ResponseBody>

    @Headers("Content-type: application/json")
    @GET("/vehicle")
    fun getVehicles(@Header("Authorization") authorization : String): Call<MutableList<VehicleModel>>
}