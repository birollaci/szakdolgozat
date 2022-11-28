package hu.bme.aut.rentapp.models

import com.google.gson.annotations.SerializedName

data class AddressModelPost(
    @SerializedName("id") val id : Int? = null,
    @SerializedName("country") val country : String? = null,
    @SerializedName("county") val county : String? = null,
    @SerializedName("city") val city : String? = null,
    @SerializedName("zipCode") val zipCode : String? = null,
    @SerializedName("streetName") val streetName : String? = null,
    @SerializedName("number") val number : String? = null,
    @SerializedName("door") val door : String? = null,
)
