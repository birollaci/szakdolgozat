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
import hu.bme.aut.rentapp.*
import hu.bme.aut.rentapp.service.ApiService
import hu.bme.aut.rentapp.data.DataManager
import hu.bme.aut.rentapp.service.ServiceGenerator
import hu.bme.aut.rentapp.models.ContractModel
import hu.bme.aut.rentapp.models.VehicleModel
import kotlinx.android.synthetic.main.activity_contract.*
import kotlinx.android.synthetic.main.activity_home.profileName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class ContractActivity : AppCompatActivity() {

    private val dialog: Dialog = Dialog()

    private lateinit var adapter: AdapterContract

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("rentapp", "Contract")
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contract)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        profileName.text = DataManager.profileNameText

        initRecyclerViewContract()

        btnDeleteAll.setOnClickListener {
            Log.d("rentapp", "deleteAll")
            deleteAll()
        }

        btnToRent.setOnClickListener {
            Log.d("rentapp", "to rent")
            startActivity(Intent(this, RentActivity::class.java))
        }
    }

    private fun deleteAll() {
        val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)
        val call = serviceGenerator.emptyContract(DataManager.bearerToken)
        call.enqueue(object : Callback<ContractModel> {
            override fun onResponse(
                call: Call<ContractModel>,
                response: Response<ContractModel>
            ) {
                if(response.isSuccessful) {
                    if (response.body() != null) {
                        Log.d("rentapp", response.body().toString())
                        deleteOk(response.code())
                        initRecyclerViewContract()
                    }else{
                        Log.d("rentapp", "empty")
                    }
                }
            }

            override fun onFailure(call: Call<ContractModel>, t: Throwable) {
                Log.d("welcome error", t.message.toString())
                deleteOk(404)
            }
        })
        initRecyclerViewContract()
    }

    private fun initRecyclerViewContract() {
        val MRecyclerVContract = findViewById<RecyclerView>(R.id.MRecyclerVContract)
        MRecyclerVContract.layoutManager = LinearLayoutManager(this)
        adapter = AdapterContract()

        uploadAll(adapter)

        MRecyclerVContract.adapter = adapter
    }

    private fun uploadAll(adapter: AdapterContract) {
        val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)
        val call = serviceGenerator.getContract(DataManager.bearerToken)
        call.enqueue(object : Callback<ContractModel> {
            override fun onResponse(
                call: Call<ContractModel>,
                response: Response<ContractModel>
            ) {
                if(response.isSuccessful) {
                    if (response.body() != null) {
                        for (item in response.body()!!.vehicles!!) {
                            adapter.add(item)
                        }
                    }else{
                        Log.d("rentapp", "empty")
                    }
                }
            }

            override fun onFailure(call: Call<ContractModel>, t: Throwable) {
                Log.d("welcome error", t.message.toString())
            }
        })
    }

    private fun delete(id: Long?) {
        Log.d("rentapp", "delete")
        val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)
        val call = serviceGenerator.deleteVehicleFromContractById(DataManager.bearerToken, id)
        call.enqueue(object : Callback<ContractModel> {
            override fun onResponse(call: Call<ContractModel>, response: Response<ContractModel>) {
                if(response.isSuccessful) {
                    if (response.body() != null) {
                        Log.d("rentapp", response.body().toString())
                        deleteOk(response.code())
                    }else{
                        Log.d("rentapp", "empty")
                    }
                }
            }

            override fun onFailure(call: Call<ContractModel>, t: Throwable) {
                Log.d("welcome error", t.message.toString())
                deleteOk(404)
            }
        })
        initRecyclerViewContract()
    }

    private fun deleteOk(code: Int) {
        if(code == 200)
            dialog.showDefaultDialog(this, "The delete was successful!", "Info")
        else
            dialog.showDefaultDialog(this, "Delete failed!", "Alert")
    }

    inner class AdapterContract : RecyclerView.Adapter<AdapterContract.ViewHolder>() {

        private val items: MutableList<VehicleModel>
        init {
            items = ArrayList()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_contract, parent, false)
            return ViewHolder(view)
        }

        fun add(new: VehicleModel) {
            items.add(new)
            notifyItemInserted(itemCount - 1)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemName.text = items[position].name
            holder.itemDetails.setOnClickListener {
                Log.d("rentapp", "itemdetails $position")
                DataManager.vehicleId = items[position].id
                startActivity(Intent(this@ContractActivity, DetailsActivity::class.java))
            }
            holder.itemDelete.setOnClickListener {
                Log.d("rentapp", "itemdelete $position")
                delete(items[position].id)
            }
        }

        override fun getItemCount(): Int {
            return items.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var itemName = itemView.findViewById<TextView>(R.id.item_name_contract)
            var itemDetails = itemView.findViewById<Button>(R.id.item_button_contract_details)
            var itemDelete = itemView.findViewById<Button>(R.id.item_button_contract)
        }
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