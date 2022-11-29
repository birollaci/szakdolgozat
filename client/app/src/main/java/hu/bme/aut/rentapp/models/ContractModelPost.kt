package hu.bme.aut.rentapp.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class ContractModelPost(
    @SerializedName("id") val id : Int? = null,
    @SerializedName("vehicles") val vehicles : List<VehicleModel>? = null,
    @SerializedName("startDate") val startDate: Date? = null,
    @SerializedName("endDate") val endDate: Date? = null,
)
