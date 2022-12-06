package hu.bme.aut.rentapp.activity

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
import hu.bme.aut.rentapp.Dialog
import hu.bme.aut.rentapp.R
import hu.bme.aut.rentapp.service.ApiService
import hu.bme.aut.rentapp.data.DataManager
import hu.bme.aut.rentapp.service.ServiceGenerator
import hu.bme.aut.rentapp.models.ContractModel
import hu.bme.aut.rentapp.models.VehicleModel
import kotlinx.android.synthetic.main.activity_category.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryActivity : AppCompatActivity() {

    private val dialog: Dialog = Dialog()

    private lateinit var adapter: AdapterCategory

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("rentapp", "CategoryActivity")
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
        adapter = AdapterCategory()

        uploadAll(adapter)

        MainRecyclerView.adapter = adapter
    }

    private fun uploadAll(adapter: AdapterCategory) {
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
                        Log.d("rentapp", "empty")
                    }
                }
            }

            override fun onFailure(call: Call<MutableList<VehicleModel>>, t: Throwable) {
                Log.d("rentapperror", t.message.toString())
            }
        })
    }

    private fun uploadByCategory(adapter: AdapterCategory, category: String) {
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
                        Log.d("rentapp", "empty")
                    }
                }
            }

            override fun onFailure(call: Call<MutableList<VehicleModel>>, t: Throwable) {
                Log.d("rentapperror", t.message.toString())
            }
        })
    }

    private fun addVechicleToContract(id: Long?) {
        Log.d("rentapp", "delete")
        val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)
        val call = serviceGenerator.addVehicleToContractById(DataManager.bearerToken, id)
        call.enqueue(object : Callback<ContractModel> {
            override fun onResponse(call: Call<ContractModel>, response: Response<ContractModel>) {
                if(response.isSuccessful) {
                    if (response.body() != null) {
                        Log.d("rentapp", response.body().toString())
                    }else{
                        Log.d("rentapp", "empty")
                    }
                }
                addOk(response.code())
            }

            override fun onFailure(call: Call<ContractModel>, t: Throwable) {
                Log.d("rentapp error", t.message.toString())
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

    inner class AdapterCategory : RecyclerView.Adapter<AdapterCategory.ViewHolder>() {

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
                Log.d("rentapp", "item $position")
                DataManager.vehicleId = items[position].id
                startActivity(Intent(this@CategoryActivity, DetailsActivity::class.java))
            }
            holder.itemAdd.setOnClickListener {
                Log.d("rentapp", "itemAdd $position")
                addVechicleToContract(items[position].id)
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
        adapter = AdapterCategory()

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