package hu.bme.aut.rentapp.models

data class AddressModel (
    val id : Long? = null,
    val country : String? = null,
    val county : String? = null,
    val city : String? = null,
    val zipCode : String? = null,
    val streetName : String? = null,
    val number : String? = null,
    val door : String? = null,
)