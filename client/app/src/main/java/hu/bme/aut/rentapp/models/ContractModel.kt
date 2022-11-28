package hu.bme.aut.rentapp.models

import java.util.Date

data class ContractModel(
    val id: Int? = null,
    val vehicles: List<VehicleModel>? = null,
    val startDate: Date? = null,
    val endDate: Date? = null,
)
