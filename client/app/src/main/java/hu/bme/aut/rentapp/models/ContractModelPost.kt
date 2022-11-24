package hu.bme.aut.rentapp.models

data class ContractModelPost(
    val id : Int? = null,
    val vehicles : List<VehicleModel>? = null,
)
