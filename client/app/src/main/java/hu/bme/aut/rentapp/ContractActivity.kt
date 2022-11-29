package hu.bme.aut.rentapp

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.rentapp.data.DataManager
import kotlinx.android.synthetic.main.activity_home.*

class ContractActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("welcome", "Contract")
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contract)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        profileName.text = DataManager.profileNameText
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_contract, menu)
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
            R.id.delete_all -> {
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