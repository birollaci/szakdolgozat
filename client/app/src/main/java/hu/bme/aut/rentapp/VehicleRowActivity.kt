package hu.bme.aut.rentapp

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.rentapp.data.DataManager
import hu.bme.aut.rentapp.data.ServiceGenerator
import hu.bme.aut.rentapp.models.VehicleModel
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.vehicle_row.*
import kotlinx.android.synthetic.main.vehicle_row.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VehicleRowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("welcome", "VehicleActivity")
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vehicle_row)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        val rowItem = LayoutInflater.from(this).inflate(R.layout.vehicle_row, null)
        rowItem.btnDetails.setOnClickListener {
            Log.d("welcome", "details")
            Log.d("welcome", rowItem.vehicle_id.toString())
        }

        rowItem.btnAdd.setOnClickListener {
            Log.d("welcome", "add")
            Log.d("welcome", rowItem.vehicle_id.toString())
        }

        btnDetails.setOnClickListener {
            Log.d("welcome", "details")
            Log.d("welcome", rowItem.vehicle_id.toString())
        }

        this.btnAdd.setOnClickListener {
            Log.d("welcome", "add")
            Log.d("welcome", rowItem.vehicle_id.toString())
        }
    }



    override fun onBackPressed() {
        Toast.makeText(this, "Back is blocked!!!", Toast.LENGTH_SHORT).show()
    }
}