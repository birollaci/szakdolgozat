package hu.bme.aut.rentapp

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.rentapp.data.DataManager
import hu.bme.aut.rentapp.data.ServiceGenerator
import hu.bme.aut.rentapp.models.VehicleModel
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.vehicle_row.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("welcome", "CategoryActivity")
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        profileName.text = DataManager.profileNameText

        uploadAllVehicles()

        val rowItem = LayoutInflater.from(this).inflate(R.layout.vehicle_row, null)
        rowItem.btnDetails.setOnClickListener {
            Log.d("welcome", "details")
            Log.d("welcome", rowItem.vehicle_id.toString())
        }

        rowItem.btnAdd.setOnClickListener {
            Log.d("welcome", "add")
            Log.d("welcome", rowItem.vehicle_id.toString())
        }
    }

    private fun uploadByCategory(category: String) {
        val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)
        val call = serviceGenerator.getVehiclesByCategory(DataManager.bearerToken, category)
        call.enqueue(object : Callback<MutableList<VehicleModel>> {
            override fun onResponse(
                call: Call<MutableList<VehicleModel>>,
                response: Response<MutableList<VehicleModel>>
            ) {
                if(response.isSuccessful) {
                    if (response.body() != null) {
                        for (item in response.body()!!) {
                            upload(item)
                        }
                    }else{
                        Log.d("welcome", "empty")
                    }
                }
            }

            override fun onFailure(call: Call<MutableList<VehicleModel>>, t: Throwable) {
                Log.d("welcomeerror", t.message.toString())
            }
        })
    }

    private fun uploadAllVehicles() {
        val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)
        val call = serviceGenerator.getVehicles(DataManager.bearerToken,)
        call.enqueue(object : Callback<MutableList<VehicleModel>> {
            override fun onResponse(
                call: Call<MutableList<VehicleModel>>,
                response: Response<MutableList<VehicleModel>>
            ) {
                if(response.isSuccessful) {
                    if (response.body() != null) {
                        for (item in response.body()!!) {
                            upload(item)
                        }
                    }else{
                        Log.d("welcome", "empty")
                    }
                }
            }

            override fun onFailure(call: Call<MutableList<VehicleModel>>, t: Throwable) {
                Log.d("welcomeerror", t.message.toString())
            }
        })
    }

    private fun upload(item: VehicleModel) {
        val rowItem = LayoutInflater.from(this).inflate(R.layout.vehicle_row, null)

        rowItem.vehicle_id.text = item.id.toString()
        rowItem.vehicle_name.text = item.name
        rowItem.vehicle_brand.text = item.brand
        rowItem.vehicle_price.text = item.price.toString()
        rowItem.vehicle_category.text = item.category
//        rowItem.vehicle_description.text = item.description

        when {
            item.category.toString() == "CAR" -> {
                rowItem.vehicle_image.setImageResource(R.drawable.car)
            }
            item.category.toString() == "ROLLER" -> {
                rowItem.vehicle_image.setImageResource(R.drawable.roller)
            }
            item.category.toString() == "BIKE" -> {
                rowItem.vehicle_image.setImageResource(R.drawable.bike)
            }
            item.category.toString() == "VAN" -> {
                rowItem.vehicle_image.setImageResource(R.drawable.van)
            }
            item.category.toString() == "TRUCK" -> {
                rowItem.vehicle_image.setImageResource(R.drawable.truck)
            }
            item.category.toString() == "BUS" -> {
                rowItem.vehicle_image.setImageResource(R.drawable.bus)
            }
            item.category.toString() == "MOTORCYCLE" -> {
                rowItem.vehicle_image.setImageResource(R.drawable.motorcycle)
            }
            else -> {}
        }

        val listSize: Int = list_of_rows.childCount
        list_of_rows.addView(rowItem, listSize - 1)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_category, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            R.id.category0 -> {
                list_of_rows.removeAllViews()
                uploadAllVehicles()
                true
            }

            R.id.category1 -> {
                list_of_rows.removeAllViews()
                uploadByCategory("CAR")
                true
            }
            R.id.category2 -> {
                list_of_rows.removeAllViews()
                uploadByCategory("ROLLER")
                true
            }
            R.id.category3 -> {
                list_of_rows.removeAllViews()
                uploadByCategory("BIKE")
                true
            }
            R.id.category4 -> {
                list_of_rows.removeAllViews()
                uploadByCategory("VAN")
                true
            }
            R.id.category5 -> {
                list_of_rows.removeAllViews()
                uploadByCategory("TRUCK")
                true
            }
            R.id.category6 -> {
                list_of_rows.removeAllViews()
                uploadByCategory("BUS")
                true
            }
            R.id.category7 -> {
                list_of_rows.removeAllViews()
                uploadByCategory("MOTORCYCLE")
                true
            }
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