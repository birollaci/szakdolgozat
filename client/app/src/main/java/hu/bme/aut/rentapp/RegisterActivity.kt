package hu.bme.aut.rentapp

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import hu.bme.aut.rentapp.data.ServiceGenerator
import hu.bme.aut.rentapp.models.AddressModel
import hu.bme.aut.rentapp.models.AddressModelPost
import hu.bme.aut.rentapp.models.RegisterModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.ResponseBody
import org.xml.sax.Attributes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.AttributedCharacterIterator.Attribute
import java.text.AttributedString

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)



        btnRegisterSave.setOnClickListener {
            Log.d("welcome", "register")

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
            val valid: Boolean = validateValue(editTexts)
            if (!valid) {
                showDefaultDialog(this, "Please enter the correct values!", "Alert")
            } else if (passwordReg.text.toString() != passwordReg2.text.toString()) {
                passwordReg2.requestFocus()
                passwordReg2.error = "Please enter the same password"
            }
            else {
                val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)

                val homeAddressExternal = AddressModelPost(
                    country = validateEmptyString(countryRegHome),
                    city = validateEmptyString(cityRegHome),
                    zipCode = validateEmptyString(zipCodeRegHome),
                    streetName = validateEmptyString(streetNameRegHome),
                    number = validateEmptyInt(numberRegHome),
                    county = validateEmptyString(countyRegHome),
                    door = validateEmptyString(doorRegHome),
                )

                if(billingChecker) {
                    val billingAddressExternal = AddressModelPost(
                        country = validateEmptyString(countryRegBilling),
                        city = validateEmptyString(cityRegBilling),
                        zipCode = validateEmptyString(zipCodeRegBilling),
                        streetName = validateEmptyString(streetNameRegBilling),
                        number = validateEmptyInt(numberRegBilling),
                        county = validateEmptyString(countyRegBilling),
                        door = validateEmptyString(doorRegBilling),
                    )
                    val registerModel = RegisterModel(
                        username = validateEmptyString(usernameReg),
                        password = validateEmptyString(passwordReg),
                        email = validateEmptyString(emailReg),
                        firstname = validateEmptyString(firstnameReg),
                        lastname = validateEmptyString(lastnameReg),
                        phoneNumber = validateEmptyString(phoneReg),
                        homeAddress = homeAddressExternal,
                        billingAddress = billingAddressExternal
                    )
                    Log.d("welcome", registerModel.toString())
                    call = serviceGenerator.postRegister(
                        registerModel
                    )
                } else {
                    val registerModel = RegisterModel(
                        username = validateEmptyString(usernameReg),
                        password = validateEmptyString(passwordReg),
                        email = validateEmptyString(emailReg),
                        firstname = validateEmptyString(firstnameReg),
                        lastname = validateEmptyString(lastnameReg),
                        phoneNumber = validateEmptyString(phoneReg),
                        homeAddress = homeAddressExternal,
                        billingAddress = null
                    )
                    Log.d("welcome", registerModel.toString())
                    call = serviceGenerator.postRegister(
                        registerModel
                    )
                }

                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        Log.d("welcome", "ok")
                        Log.d("welcome", response.code().toString()) // http status code (200)
                        Log.d("welcome", response.body().toString())
                        save(response.code())
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.d("welcome", t.message.toString())
                    }

                })
            }
        }
    }

    fun save(code: Int){
        if (code == 201) {
            showDefaultDialog(this, "Registration is successfully!!!", "Info")
            startActivity(Intent(this, LoginActivity::class.java))
        } else {

        }

    }

    private fun validateValue(values: List<EditText>): Boolean{
        for( value in values) {
            if (value.text.toString().isEmpty()) {
                value.requestFocus()
                value.error = "Please enter the value"
                return false
            }
        }
        return true
    }

    private fun showDefaultDialog(context: Context, str: String, title: String) {
        val alertDialog = AlertDialog.Builder(context)

        alertDialog.apply {
            setIcon(R.drawable.danger_sign)
            setTitle(title)
            setMessage(str)
            setPositiveButton("Close") { _: DialogInterface?, _: Int ->
                Toast.makeText(context, "All right", Toast.LENGTH_SHORT).show()
            }

        }.create().show()
    }

    private fun validateEmptyString(editText: EditText): String? {
        if(editText.text.toString() == "") {
            return null
        }
        return editText.text.toString()
    }

    private fun validateEmptyInt(editText: EditText): Int? {
        if(editText.text.toString() == "") {
            return null
        }
        return editText.text.toString().toInt()
    }

}