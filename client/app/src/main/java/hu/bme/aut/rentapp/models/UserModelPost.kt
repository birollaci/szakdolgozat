package hu.bme.aut.rentapp.models

import com.google.gson.annotations.SerializedName

data class UserModelPost(
    @SerializedName("username") val username : String? = null,
    @SerializedName("password") val password : String? = null,
    @SerializedName("email") val email : String? = null,
    @SerializedName("firstName") val firstName : String? = null,
    @SerializedName("lastName") val lastName : String? = null,
    @SerializedName("phoneNumber") val phoneNumber : String? = null,
    @SerializedName("homeAddress") val homeAddress : AddressModelPost? = null,
    @SerializedName("billingAddress") val billingAddress : AddressModelPost? = null,
    @SerializedName("contract") val contract : ContractModelPost? = null,
)
