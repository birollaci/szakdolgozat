package hu.bme.aut.rentapp

import hu.bme.aut.rentapp.models.*
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
    fun postLogin(@Body loginModel: LoginModel?): Call<ResponseBody>

    @Headers("Content-type: application/json")
    @POST("/register")
    fun postRegister(@Body registerModel: RegisterModel?): Call<ResponseBody>

    @GET("/vehicle")
    fun getVehicles(@Header("Authorization") authorization: String?): Call<MutableList<VehicleModel>>


    @GET("/vehicle/category/{category}")
    fun getVehiclesByCategory(@Header("Authorization") authorization: String?, @Path("category") category: String?): Call<MutableList<VehicleModel>>

    @GET("/user")
    fun getUsers(@Header("Authorization") authorization: String?): Call<UserModel>

    @Headers("Content-type: application/json")
    @PUT("/user")
    fun updateUser(@Header("Authorization") authorization: String?, @Body userModel: UserModelPost?): Call<UserModel>

    @DELETE("/user")
    fun deleteUser(@Header("Authorization") authorization: String?): Call<ResponseBody>
}