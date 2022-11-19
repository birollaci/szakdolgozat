package hu.bme.aut.rentapp.models

data class VehicleModel(
    val id : Int? = null,
    val name : String? = null,
    val brand : String? = null,
    val price : Double? = null,
    val description : String? = null,
    val category: String? = null,
)