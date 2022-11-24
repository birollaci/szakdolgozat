package hu.bme.aut.rentapp.models

data class UserModel(
    val username : String? = null,
    val password : String? = null,
    val email : String? = null,
    val firstname : String? = null,
    val lastname : String? = null,
    val phoneNumber : String? = null,
    val homeAddress : AddressModel? = null,
    val billingAddress : AddressModel? = null,
    val contract : ContractModel? = null,
)
