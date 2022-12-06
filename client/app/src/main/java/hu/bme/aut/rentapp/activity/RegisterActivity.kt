package hu.bme.aut.rentapp.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import hu.bme.aut.rentapp.Dialog
import hu.bme.aut.rentapp.R
import hu.bme.aut.rentapp.service.ApiService
import hu.bme.aut.rentapp.service.ServiceGenerator
import hu.bme.aut.rentapp.models.AddressModelPost
import hu.bme.aut.rentapp.models.RegisterModel
import hu.bme.aut.rentapp.validations.Validation
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    val dialog: Dialog = Dialog()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Log.d("rentapp", "RegisterActivity")

        btnRegisterSave.setOnClickListener {
            Log.d("rentapp", "register")

            saveRegister()
        }
    }

    private fun saveRegister(){
        val validation: Validation = Validation()
        var editTexts: List<EditText> = listOf(
            usernameReg,
            passwordReg,
            passwordReg2,
            emailReg,
            firstnameReg,
            lastnameReg,
            phoneReg,
            countryRegHome,
            cityRegHome,
            zipCodeRegHome,
            streetNameRegHome,
            numberRegHome,
        )

        var call: Call<ResponseBody>
        var billingChecker: Boolean = false
        if(countryRegBilling.text.toString().isNotEmpty() ||
            cityRegBilling.text.toString().isNotEmpty() ||
            zipCodeRegBilling.text.toString().isNotEmpty() ||
            streetNameRegBilling.text.toString().isNotEmpty() ||
            numberRegBilling.text.toString().isNotEmpty() ||
            countyRegBilling.text.toString().isNotEmpty() ||
            doorRegBilling.text.toString().isNotEmpty()
        ) {
            billingChecker = true
            editTexts = listOf(
                usernameReg,
                passwordReg,
                passwordReg2,
                emailReg,
                firstnameReg,
                lastnameReg,
                phoneReg,
                countryRegHome,
                cityRegHome,
                zipCodeRegHome,
                streetNameRegHome,
                numberRegHome,
                countryRegBilling,
                cityRegBilling,
                zipCodeRegBilling,
                streetNameRegBilling,
                numberRegBilling,
            )
        }
        val valid: Boolean = validation.validateEditTexts(editTexts)
        if (!valid) {
            dialog.showDefaultDialog(this, "Please enter the correct values!", "Alert")
        } else if (passwordReg.text.toString() != passwordReg2.text.toString()) {
            passwordReg2.requestFocus()
            passwordReg2.error = "Please enter the same password"
            dialog.showDefaultDialog(this, "Please enter the same password", "Alert")
        }
        else if (!validation.validationUsername(usernameReg)) {
            usernameReg.requestFocus()
            usernameReg.error = "Please enter the correct password"
            dialog.showDefaultDialog(this, "Username is incorrect! Min 3 and max 24 character", "Alert")
        }
        else if (!validation.validationPassword(passwordReg)) {
            passwordReg.requestFocus()
            passwordReg.error = "Please enter the correct password"
            passwordReg2.requestFocus()
            passwordReg2.error = "Please enter the correct password"
            dialog.showDefaultDialog(this, "Password is incorrect! Min 4 and max 24 character! It must contain an uppercase letter, a lowercase letter, a number, and a special character!", "Alert")
        }
        else if (!validation.validationEmail(emailReg)) {
            emailReg.requestFocus()
            emailReg.error = "Please enter the correct password"
            dialog.showDefaultDialog(this, "Email is incorrect! Correct form: a@b.c", "Alert")
        }
        else {
            val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)

            val homeAddressExternal = AddressModelPost(
                country = validation.validateEmptyString(countryRegHome),
                city = validation.validateEmptyString(cityRegHome),
                zipCode = validation.validateEmptyString(zipCodeRegHome),
                streetName = validation.validateEmptyString(streetNameRegHome),
                number = validation.validateEmptyString(numberRegHome),
                county = validation.validateEmptyString(countyRegHome),
                door = validation.validateEmptyString(doorRegHome),
            )

            if(billingChecker) {
                val billingAddressExternal = AddressModelPost(
                    country = validation.validateEmptyString(countryRegBilling),
                    city = validation.validateEmptyString(cityRegBilling),
                    zipCode = validation.validateEmptyString(zipCodeRegBilling),
                    streetName = validation.validateEmptyString(streetNameRegBilling),
                    number = validation.validateEmptyString(numberRegBilling),
                    county = validation.validateEmptyString(countyRegBilling),
                    door = validation.validateEmptyString(doorRegBilling),
                )
                val registerModel = RegisterModel(
                    username = validation.validateEmptyString(usernameReg),
                    password = validation.validateEmptyString(passwordReg),
                    email = validation.validateEmptyString(emailReg),
                    firstName = validation.validateEmptyString(firstnameReg),
                    lastName = validation.validateEmptyString(lastnameReg),
                    phoneNumber = validation.validateEmptyString(phoneReg),
                    homeAddress = homeAddressExternal,
                    billingAddress = billingAddressExternal
                )
                Log.d("rentapp", registerModel.toString())
                call = serviceGenerator.postRegister(
                    registerModel
                )
            } else {
                val registerModel = RegisterModel(
                    username = validation.validateEmptyString(usernameReg),
                    password = validation.validateEmptyString(passwordReg),
                    email = validation.validateEmptyString(emailReg),
                    firstName = validation.validateEmptyString(firstnameReg),
                    lastName = validation.validateEmptyString(lastnameReg),
                    phoneNumber = validation.validateEmptyString(phoneReg),
                    homeAddress = homeAddressExternal,
                    billingAddress = null
                )
                Log.d("rentapp", registerModel.toString())
                call = serviceGenerator.postRegister(
                    registerModel
                )
            }

            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Log.d("rentapp", "ok")
                    Log.d("rentapp", response.code().toString()) // http status code (200)
                    Log.d("rentapp", response.body().toString())
                    goToLogin(response.code())
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("rentapp", t.message.toString())
                }

            })
        }
    }

    fun goToLogin(code: Int){
        if (code == 201) {
            dialog.showDefaultDialog(this, "Registration is successfully!!!", "Info")
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            dialog.showDefaultDialog(this, "Registration problem! ResponseCode: $code", "Alert")
        }

    }

    

}