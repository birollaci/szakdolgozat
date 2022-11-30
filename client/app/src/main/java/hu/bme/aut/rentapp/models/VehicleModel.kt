package hu.bme.aut.rentapp.models

data class VehicleModel(
    val id : Long? = null,
    val name : String? = null,
    val brand : String? = null,
    val price : Int? = null,
    val description : String? = null,
    val category: String? = null,
)