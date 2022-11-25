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
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)
        Log.d("welcome", "ok")
        val call = serviceGenerator.getVehicles(DataManager.bearerToken)
        call.enqueue(object : Callback<MutableList<VehicleModel>> {
            override fun onResponse(
                call: Call<MutableList<VehicleModel>>,
                response: Response<MutableList<VehicleModel>>
            ) {
                Log.d("welcome", "ok")
                Log.d("welcome", response.code().toString()) // http status code (200)
                Log.d("welcome", response.body().toString())
                for(item in response.body()!!) {
                    Log.d("welcome", item.toString())
                    Log.d("welcome", item.id.toString())
                    Log.d("welcome", item.name.toString())
                    Log.d("welcome", item.price.toString())
                    Log.d("welcome", item.description.toString())
                    Log.d("welcome", item.category.toString())

                    upload(item)
                }
            }

            override fun onFailure(call: Call<MutableList<VehicleModel>>, t: Throwable) {
                Log.d("welcomeerror", t.message.toString())
            }
        })
    }

    private fun upload(item: VehicleModel) {
        val rowItem = LayoutInflater.from(this).inflate(R.layout.vehicle_row, null)

        rowItem.vehicle_name.text = item.name
        rowItem.vehicle_price.text = item.price.toString()
        rowItem.vehicle_description.text = item.description

        val listSize: Int = list_of_rows.childCount
        list_of_rows.addView(rowItem, listSize - 1)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_category, menu)
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