package hu.bme.aut.rentapp.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.rentapp.Dialog
import hu.bme.aut.rentapp.R
import hu.bme.aut.rentapp.service.ApiService
import hu.bme.aut.rentapp.data.DataManager
import hu.bme.aut.rentapp.service.ServiceGenerator
import hu.bme.aut.rentapp.models.*
import hu.bme.aut.rentapp.validations.Validation
import kotlinx.android.synthetic.main.activity_settings.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingsActivity : AppCompatActivity() {

    val dialog: Dialog = Dialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("welcome", "SettingsActivity")
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) 

        profileName.text = DataManager.profileNameText

        val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)
        val call = serviceGenerator.getUsers(DataManager.bearerToken)
        call.enqueue(object : Callback<UserModel> {
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                if(response.isSuccessful) {
                    if (response.body() != null) {
                        Log.d("welcome", response.body().toString())
                        uploadSettings(response.body()!!)
                    }else{
                        Log.d("welcome", "empty")
                    }
                }
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Log.d("welcomeerror", t.message.toString())
            }
        })

        btnSettingsUpdate.setOnClickListener {
            Log.d("welcome", "update")
            updateUser()
        }

        btnSettingsDelete.setOnClickListener {
            Log.d("welcome", "delete")
            val deleteCall = serviceGenerator.deleteUser(DataManager.bearerToken)

            deleteCall.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Log.d("welcome", response.isSuccessful.toString())
                    if(response.isSuccessful){
                        deleteNoContent(response.code())
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("welcome", t.message.toString())
                    deleteNoContent(404)
                }
            })
        }

    }

    private fun deleteNoContent(code: Int) {
        if(code == 204) {
            DataManager.bearerToken = ""
            dialog.showDefaultDialog(this, "The delete was successful!", "Info")
            startActivity(Intent(this, LoginActivity::class.java))
        } else
            dialog.showDefaultDialog(this, "Delete fail! Response code: $code" , "Alert")
    }

    private fun updateUser() {
        val validation: Validation = Validation()
        var validHome: Boolean = true
        var validBilling: Boolean = true

        if(countrySettingsHome.text.toString().isNotEmpty() ||
            citySettingsHome.text.toString().isNotEmpty() ||
            zipCodeSettingsHome.text.toString().isNotEmpty() ||
            streetNameSettingsHome.text.toString().isNotEmpty() ||
            numberSettingsHome.text.toString().isNotEmpty() ||
            countySettingsHome.text.toString().isNotEmpty() ||
            doorSettingsHome.text.toString().isNotEmpty()
        ) {
            val editTexts: List<EditText> = listOf(
                countrySettingsHome,
                citySettingsHome,
                zipCodeSettingsHome,
                streetNameSettingsHome,
                numberSettingsHome,
            )
            validHome = validation.validateEditTexts(editTexts)
        }

        var billingChecker: Boolean = false
        if(countrySettingsBilling.text.toString().isNotEmpty() ||
            citySettingsBilling.text.toString().isNotEmpty() ||
            zipCodeSettingsBilling.text.toString().isNotEmpty() ||
            streetNameSettingsBilling.text.toString().isNotEmpty() ||
            numberSettingsBilling.text.toString().isNotEmpty() ||
            countySettingsBilling.text.toString().isNotEmpty() ||
            doorSettingsBilling.text.toString().isNotEmpty()
        ) {
            val editTexts: List<EditText> = listOf(
                countrySettingsBilling,
                citySettingsBilling,
                zipCodeSettingsBilling,
                streetNameSettingsBilling,
                numberSettingsBilling,
            )
            billingChecker = true
            validBilling = validation.validateEditTexts(editTexts)
        }

        var call: Call<UserModel>

        if (!validHome || !validBilling) {
            dialog.showDefaultDialog(this, "Home or Billing address are incorrect!", "Alert")
        }
        // Ha van benne valami es nem felel meg az elvarasoknak
        else if (usernameSettings.text.toString().isNotEmpty() && !validation.validationUsername(usernameSettings)) {
            usernameSettings.requestFocus()
            usernameSettings.error = "Please enter the correct password"
            dialog.showDefaultDialog(this, "Username is incorrect! Min 3 and max 24 character", "Alert")
        }
        else if (passwordSettings.text.toString().isNotEmpty() && !validation.validationPassword(passwordSettings)) {
            passwordSettings.requestFocus()
            passwordSettings.error = "Please enter the correct password"
            dialog.showDefaultDialog(this, "Password is incorrect! Min 4 and max 24 character! It must contain an uppercase letter, a lowercase letter, a number, and a special character!", "Alert")
        }
        else if (emailSettings.text.toString().isNotEmpty() && !validation.validationEmail(emailSettings)) {
            emailSettings.requestFocus()
            emailSettings.error = "Please enter the correct password"
            dialog.showDefaultDialog(this, "Email is incorrect! Correct form: a@b.c", "Alert")
        }
        else {
            val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)

            val homeAddressExternal = AddressModelPost(
                country = validation.validateEmptyString(countrySettingsHome),
                city = validation.validateEmptyString(citySettingsHome),
                zipCode = validation.validateEmptyString(zipCodeSettingsHome),
                streetName = validation.validateEmptyString(streetNameSettingsHome),
                number = validation.validateEmptyString(numberSettingsHome),
                county = validation.validateEmptyString(countySettingsHome),
                door = validation.validateEmptyString(doorSettingsHome),
            )

            if(billingChecker) {
                val billingAddressExternal = AddressModelPost(
                    country = validation.validateEmptyString(countrySettingsBilling),
                    city = validation.validateEmptyString(citySettingsBilling),
                    zipCode = validation.validateEmptyString(zipCodeSettingsBilling),
                    streetName = validation.validateEmptyString(streetNameSettingsBilling),
                    number = validation.validateEmptyString(numberSettingsBilling),
                    county = validation.validateEmptyString(countySettingsBilling),
                    door = validation.validateEmptyString(doorSettingsBilling),
                )
                val userModelPost = UserModelPost(
                    username = validation.validateEmptyString(usernameSettings),
                    password = validation.validateEmptyString(passwordSettings),
                    email = validation.validateEmptyString(emailSettings),
                    firstName = validation.validateEmptyString(firstnameSettings),
                    lastName = validation.validateEmptyString(lastnameSettings),
                    phoneNumber = validation.validateEmptyString(phoneSettings),
                    homeAddress = homeAddressExternal,
                    billingAddress = billingAddressExternal
                )
                Log.d("welcome", userModelPost.toString())
                call = serviceGenerator.updateUser(
                    DataManager.bearerToken,
                    userModelPost
                )
            } else {
                val userModelPost = UserModelPost(
                    username = validation.validateEmptyString(usernameSettings),
                    password = validation.validateEmptyString(passwordSettings),
                    email = validation.validateEmptyString(emailSettings),
                    firstName = validation.validateEmptyString(firstnameSettings),
                    lastName = validation.validateEmptyString(lastnameSettings),
                    phoneNumber = validation.validateEmptyString(phoneSettings),
                    homeAddress = homeAddressExternal,
                    billingAddress = null
                )
                Log.d("welcome", userModelPost.toString())
                call = serviceGenerator.updateUser(
                    DataManager.bearerToken,
                    userModelPost
                )
            }

            call.enqueue(object : Callback<UserModel> {
                override fun onResponse(
                    call: Call<UserModel>,
                    response: Response<UserModel>
                ) {
                    Log.d("welcome", "ok")
                    Log.d("welcome", response.code().toString()) // http status code (200)
                    Log.d("welcome", response.body().toString())
                    updateSuccess(response.code())
                }

                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                    Log.d("welcome", t.message.toString())
                }

            })
        }
    }

    private fun updateSuccess(code: Int) {
        if(code == 200)
            dialog.showDefaultDialog(this, "Update Success", "Info")
        else
            dialog.showDefaultDialog(this, "User update problem! ResponseCode: $code", "Alert")
    }

    private fun uploadSettings(user: UserModel) {

        usernameSettings.setText(user.username, TextView.BufferType.EDITABLE)
        emailSettings.setText(user.email, TextView.BufferType.EDITABLE)
        firstnameSettings.setText(user.firstName, TextView.BufferType.EDITABLE)
        lastnameSettings.setText(user.lastName, TextView.BufferType.EDITABLE)
        phoneSettings.setText(user.phoneNumber, TextView.BufferType.EDITABLE)

        val homeAddress: AddressModel? = user.homeAddress
        val billingAddress: AddressModel? = user.billingAddress

        countrySettingsHome.setText(homeAddress?.country, TextView.BufferType.EDITABLE)
        citySettingsHome.setText(homeAddress?.city, TextView.BufferType.EDITABLE)
        zipCodeSettingsHome.setText(homeAddress?.zipCode, TextView.BufferType.EDITABLE)
        streetNameSettingsHome.setText(homeAddress?.streetName, TextView.BufferType.EDITABLE)
        countySettingsHome.setText(homeAddress?.county, TextView.BufferType.EDITABLE)
        numberSettingsHome.setText(homeAddress?.number, TextView.BufferType.EDITABLE)
        doorSettingsHome.setText(homeAddress?.door, TextView.BufferType.EDITABLE)

        countrySettingsBilling.setText(billingAddress?.country, TextView.BufferType.EDITABLE)
        citySettingsBilling.setText(billingAddress?.city, TextView.BufferType.EDITABLE)
        zipCodeSettingsBilling.setText(billingAddress?.zipCode, TextView.BufferType.EDITABLE)
        streetNameSettingsBilling.setText(billingAddress?.streetName, TextView.BufferType.EDITABLE)
        countySettingsBilling.setText(billingAddress?.county, TextView.BufferType.EDITABLE)
        numberSettingsBilling.setText(billingAddress?.number, TextView.BufferType.EDITABLE)
        doorSettingsBilling.setText(billingAddress?.door, TextView.BufferType.EDITABLE)

        Log.d("welcome", "Settings Uploaded from /user")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_settings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            R.id.to_home -> {
                startActivity(Intent(this, HomeActivity::class.java))
                true
            }
            R.id.log_out -> {
                DataManager.bearerToken = ""
                startActivity(Intent(this, LoginActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        Toast.makeText(this, "Back is blocked!!!", Toast.LENGTH_SHORT).show()
    }
}