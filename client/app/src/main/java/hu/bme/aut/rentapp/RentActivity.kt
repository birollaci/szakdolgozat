package hu.bme.aut.rentapp

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.rentapp.api.ApiService
import hu.bme.aut.rentapp.data.DataManager
import hu.bme.aut.rentapp.data.ServiceGenerator
import hu.bme.aut.rentapp.models.ContractModelPost
import hu.bme.aut.rentapp.models.LoginModel
import hu.bme.aut.rentapp.models.VehicleModel
import kotlinx.android.synthetic.main.activity_contract.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.profileName
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_rent.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class RentActivity : AppCompatActivity() {

    private val dialog: Dialog = Dialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("welcome", "Contract")
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rent)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        profileName.text = DataManager.profileNameText

        btnRentContract.setOnClickListener {
            Log.d("welcome", "rent")
            rentContract()
        }
    }

    private fun rentContract() {
        val current = Date()
        Log.d("welcome", current.toString())
        val start = datePickerStart.getDate()
        Log.d("welcome", start.toString())
        val end = datePickerEnd.getDate()
        Log.d("welcome", end.toString())

        val dateFormatedStart: String = SimpleDateFormat("yyyy-MM-dd").format(start)
        val newStartDate : Date? = SimpleDateFormat("yyyy-MM-dd").parse(dateFormatedStart)
        Log.d("welcome", newStartDate.toString())
        val dateFormatedEnd: String = SimpleDateFormat("yyyy-MM-dd").format(end)
        val newEndDate : Date? = SimpleDateFormat("yyyy-MM-dd").parse(dateFormatedEnd)
        Log.d("welcome", newEndDate.toString())

        val cmp: Int = newStartDate!!.compareTo(newEndDate)
        if(cmp > 0) {
            dialog.showDefaultDialog(this, "Start date is after end date, start date must be smaller than end date!", "Alert")
            return
        }
        if(cmp == 0) {
            dialog.showDefaultDialog(this, "Both dates are equal, start date must be smaller than end date!", "Alert")
            return
        }

        val dateFormatedCurrent: String = SimpleDateFormat("yyyy-MM-dd").format(current)
        val newCurrentDate : Date? = SimpleDateFormat("yyyy-MM-dd").parse(dateFormatedCurrent)
        Log.d("welcome", newCurrentDate.toString())

        val cmp2: Int = newCurrentDate!!.compareTo(newStartDate)

        if(cmp2 > 0) {
            dialog.showDefaultDialog(this, "Start date is before current date, start date must be greater or equal than current date!", "Alert")
            return
        }

        val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)
        val call = serviceGenerator.rent(DataManager.bearerToken, dateFormatedStart, dateFormatedEnd)
        call.enqueue(object: Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful) {
                    Log.d("welcome", response.toString())
                }
                rentOk(response.code())
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("welcome error", t.message.toString())
                rentOk(404)
            }
        })
    }

    private fun rentOk(code: Int) {
        when (code) {
            200 -> dialog.showDefaultDialog(this, "The renting mechanism was successful! Email sent!", "Info")
            400 -> dialog.showDefaultDialog(this, "There are no vehicles in the list!", "Alert")
            else -> dialog.showDefaultDialog(this, "Rent failed!", "Alert")
        }
    }

    private fun DatePicker.getDate(): Date? {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        return calendar.time
    }

}


