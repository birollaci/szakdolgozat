package hu.bme.aut.rentapp.data

object DataManager {
    var bearerToken: String? = ""
        get() = field
        set(value) {
            field = value
        }

    var store = Store(
        "", ""
    )

    var boxesarray = arrayOf<Array<Int>>()

    var inr  = arrayOf<Array<Int>>()
    var outr  = arrayOf<Array<Int>>()

    var size = 0
    // amik belefertek
    var sum = 0
    // amik nem fertek bele
    var sum2 = 0
    var empty = 0
}