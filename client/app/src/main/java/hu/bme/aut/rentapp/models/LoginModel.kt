package hu.bme.aut.rentapp.models

import com.google.gson.annotations.SerializedName

data class LoginModel (
    @SerializedName("username") val username : String? = null,
    @SerializedName("password") val password : String? = null,
)