package hu.bme.aut.rentapp

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.rentapp.api.ApiService
import hu.bme.aut.rentapp.data.DataManager
import hu.bme.aut.rentapp.data.ServiceGenerator
import hu.bme.aut.rentapp.models.ContractModel
import hu.bme.aut.rentapp.models.VehicleModel
import kotlinx.android.synthetic.main.activity_contract.*
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_home.profileName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsActivity : AppCompatActivity() {

    private val dialog: Dialog = Dialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("welcome", "Details")
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        profileName.text = DataManager.profileNameText

        uploadAllByVehicleId(DataManager.vehicleId)

        btnDetailsAdd.setOnClickListener {
            Log.d("welcome", "deleteAll")
            addVechicleToContract(DataManager.vehicleId)
        }
    }

    private fun addVechicleToContract(id: Long?) {
        Log.d("welcome", "delete")
        val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)
        val call = serviceGenerator.addVehicleToContractById(DataManager.bearerToken, id)
        call.enqueue(object : Callback<ContractModel> {
            override fun onResponse(call: Call<ContractModel>, response: Response<ContractModel>) {
                if(response.isSuccessful) {
                    if (response.body() != null) {
                        Log.d("welcome", response.body().toString())

                    }else{
                        Log.d("welcome", "empty")
                    }
                }
                addOk(response.code())
            }

            override fun onFailure(call: Call<ContractModel>, t: Throwable) {
                Log.d("welcome error", t.message.toString())
                addOk(404)
            }
        })
    }

    private fun addOk(code: Int) {
        when (code) {
            200 -> dialog.showDefaultDialog(this, "The item added to contract!", "Info")
            400 -> dialog.showDefaultDialog(this, "The item exist in contract!", "Alert")
            else -> dialog.showDefaultDialog(this, "Add mechanism failed!", "Alert")
        }
    }

    private fun uploadAllByVehicleId(id: Long?) {
        val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)
        val call = serviceGenerator.getVehicleById(DataManager.bearerToken, id)
        call.enqueue(object : Callback<VehicleModel> {
            override fun onResponse(
                call: Call<VehicleModel>,
                response: Response<VehicleModel>
            ) {
                if(response.isSuccessful) {
                    if (response.body() != null) {
                        Log.d("welcome", response.body().toString())
                        uploadDetails(response.body()!!)
                    }else{
                        Log.d("welcome", "empty")
                    }
                }
            }

            override fun onFailure(call: Call<VehicleModel>, t: Throwable) {
                Log.d("welcomeerror", t.message.toString())
            }
        })
    }

    private fun uploadDetails(item: VehicleModel) {

        vehicle_name.text = item.name
        vehicle_brand.text = item.brand
        vehicle_price.text = item.price.toString()
        vehicle_category.text = item.category
        vehicle_description.text = item.description

        when {
            item.category.toString() == "CAR" -> {
                vehicle_image.setImageResource(R.drawable.car)
            }
            item.category.toString() == "ROLLER" -> {
                vehicle_image.setImageResource(R.drawable.roller)
            }
            item.category.toString() == "BIKE" -> {
                vehicle_image.setImageResource(R.drawable.bike)
            }
            item.category.toString() == "VAN" -> {
                vehicle_image.setImageResource(R.drawable.van)
            }
            item.category.toString() == "TRUCK" -> {
                vehicle_image.setImageResource(R.drawable.truck)
            }
            item.category.toString() == "BUS" -> {
                vehicle_image.setImageResource(R.drawable.bus)
            }
            item.category.toString() == "MOTORCYCLE" -> {
                vehicle_image.setImageResource(R.drawable.motorcycle)
            }
            else -> {}
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            R.id.to_home -> {
                startActivity(Intent(this, HomeActivity::class.java))
                true
            }
            R.id.to_category -> {
                startActivity(Intent(this, CategoryActivity::class.java))
                true
            }
            R.id.to_contract -> {
                startActivity(Intent(this, ContractActivity::class.java))
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
}