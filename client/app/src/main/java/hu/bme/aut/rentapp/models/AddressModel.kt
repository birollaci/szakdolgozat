package hu.bme.aut.rentapp.models

data class AddressModel (
    val id : Int? = null,
    val country : String? = null,
    val county : String? = null,
    val city : String? = null,
    val zipCode : String? = null,
    val streetName : String? = null,
    val number : Int? = null,
    val door : String? = null,
)