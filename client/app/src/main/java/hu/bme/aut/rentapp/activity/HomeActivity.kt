package hu.bme.aut.rentapp.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import hu.bme.aut.rentapp.*
import hu.bme.aut.rentapp.api.ApiService
import hu.bme.aut.rentapp.data.DataManager
import hu.bme.aut.rentapp.service.ServiceGenerator
import hu.bme.aut.rentapp.models.ContractModel
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {
    private val dialog: Dialog = Dialog()
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("welcome", "HomeActivity")
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        profileName.text = DataManager.profileNameText

        if (ContextCompat.checkSelfPermission(this@HomeActivity, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this@HomeActivity,
                arrayOf(android.Manifest.permission.CAMERA),
                1001
            )
        }

        btnHomeImage.setOnClickListener {
            goTosScan()
        }

        btnManualIdentifier.setOnClickListener {
            val manualIdStr: String = manualIdentifier.text.toString()
            val manualId: Long = manualIdStr.toLong()
            addVechicleToContract(manualId)
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

    private fun goTosScan() {
        startActivity(Intent(this, ScanActivity::class.java))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            R.id.to_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
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

    override fun onBackPressed() {
        Toast.makeText(this, "Back is blocked!!!", Toast.LENGTH_SHORT).show()
    }
}