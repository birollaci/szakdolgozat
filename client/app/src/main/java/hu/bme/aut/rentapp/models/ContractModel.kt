package hu.bme.aut.rentapp.models

data class ContractModel(
    val id : Int? = null,
    val vehicles : List<VehicleModel>? = null,
)
