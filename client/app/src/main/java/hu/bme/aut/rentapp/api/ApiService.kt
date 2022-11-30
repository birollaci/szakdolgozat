package hu.bme.aut.rentapp.api

import hu.bme.aut.rentapp.models.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import java.util.Date

interface ApiService {

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

    @GET("/vehicle/{vehicleId}")
    fun getVehicleById(@Header("Authorization") authorization: String?, @Path("vehicleId") vehicleId: Long?): Call<VehicleModel>

    @GET("/user")
    fun getUsers(@Header("Authorization") authorization: String?): Call<UserModel>

    @Headers("Content-type: application/json")
    @PUT("/user")
    fun updateUser(@Header("Authorization") authorization: String?, @Body userModel: UserModelPost?): Call<UserModel>

    @DELETE("/user")
    fun deleteUser(@Header("Authorization") authorization: String?): Call<ResponseBody>

    @GET("/contract")
    fun getContract(@Header("Authorization") authorization: String?): Call<ContractModel>

    @DELETE("/contract")
    fun emptyContract(@Header("Authorization") authorization: String?): Call<ContractModel>

    @PUT("/contract/{vehicleId}")
    fun addVehicleToContractById(@Header("Authorization") authorization: String?, @Path("vehicleId") vehicleId: Long?): Call<ContractModel>

    @DELETE("/contract/{vehicleId}")
    fun deleteVehicleFromContractById(@Header("Authorization") authorization: String?, @Path("vehicleId") vehicleId: Long?): Call<ContractModel>

    @POST("/contract/rent/{start}/{end}")
    fun rent(@Header("Authorization") authorization: String?, @Path("start") start: String?, @Path("end") end: String?): Call<ResponseBody>
}