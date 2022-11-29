package hu.bme.aut.rentapp

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.rentapp.data.DataManager
import hu.bme.aut.rentapp.data.ServiceGenerator
import hu.bme.aut.rentapp.models.VehicleModel
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.item.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryActivity : AppCompatActivity() {

    private lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("welcome", "CategoryActivity")
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        profileName.text = DataManager.profileNameText

        initRecyclerView()
    }

    private fun initRecyclerView() {
        val MainRecyclerView = findViewById<RecyclerView>(R.id.MainRecyclerView)
        MainRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = Adapter()

        uploadAll(adapter)

        MainRecyclerView.adapter = adapter
    }

    private fun uploadAll(adapter: Adapter) {
        val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)
        val call = serviceGenerator.getVehicles(DataManager.bearerToken)
        call.enqueue(object : Callback<MutableList<VehicleModel>> {
            override fun onResponse(
                call: Call<MutableList<VehicleModel>>,
                response: Response<MutableList<VehicleModel>>
            ) {
                if(response.isSuccessful) {
                    if (response.body() != null) {
                        for (item in response.body()!!) {
                            adapter.add(item)
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

    private fun uploadByCategory(adapter: Adapter, category: String) {
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
                            adapter.add(item)
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

    inner class Adapter : RecyclerView.Adapter<Adapter.ViewHolder>() {

        private val items: MutableList<VehicleModel>
        init {
            items = ArrayList()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
            return ViewHolder(view)
        }

        fun add(new: VehicleModel) {
            items.add(new)
            // beszuras
            notifyItemInserted(itemCount - 1)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemId.text = items[position].id.toString()
            holder.itemName.text = items[position].name
            holder.itemDetails.setOnClickListener {
                Log.d("welcome", "item $position")
                DataManager.vehicleId = items[position].id
                startActivity(Intent(this@CategoryActivity, DetailsActivity::class.java))
            }
            holder.itemAdd.setOnClickListener {
                Log.d("welcome", "itemAdd $position")

            }
        }

        override fun getItemCount(): Int {
            return items.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var itemId = itemView.findViewById<TextView>(R.id.item_id)
            var itemName = itemView.findViewById<TextView>(R.id.item_name)
            var itemDetails = itemView.findViewById<Button>(R.id.item_button)
            var itemAdd = itemView.findViewById<Button>(R.id.item_button2)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_category, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val MainRecyclerView = findViewById<RecyclerView>(R.id.MainRecyclerView)
        MainRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = Adapter()

        return when (item.getItemId()) {
            R.id.category0 -> {
                uploadAll(adapter)
                MainRecyclerView.adapter = adapter
                true
            }

            R.id.category1 -> {
                uploadByCategory(adapter, "CAR")
                MainRecyclerView.adapter = adapter
                true
            }
            R.id.category2 -> {
                uploadByCategory(adapter, "ROLLER")
                MainRecyclerView.adapter = adapter
                true
            }
            R.id.category3 -> {
                uploadByCategory(adapter, "BIKE")
                MainRecyclerView.adapter = adapter
                true
            }
            R.id.category4 -> {
                uploadByCategory(adapter, "VAN")
                MainRecyclerView.adapter = adapter
                true
            }
            R.id.category5 -> {
                uploadByCategory(adapter, "TRUCK")
                MainRecyclerView.adapter = adapter
                true
            }
            R.id.category6 -> {
                uploadByCategory(adapter, "BUS")
                MainRecyclerView.adapter = adapter
                true
            }
            R.id.category7 -> {
                uploadByCategory(adapter, "MOTORCYCLE")
                MainRecyclerView.adapter = adapter
                true
            }
            R.id.to_home -> {
                startActivity(Intent(this, HomeActivity::class.java))
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