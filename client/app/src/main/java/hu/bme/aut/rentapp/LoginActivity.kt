package hu.bme.aut.rentapp

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.rentapp.data.ServiceGenerator
import hu.bme.aut.rentapp.models.LoginModel
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class LoginActivity : AppCompatActivity() {

    var bearerToken: String? = ""
        get() = field
        set(value) {
            field = value
        }
    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)
//        val call = serviceGenerator.getPosts()
//        val call = serviceGenerator.getWelcome()




        btnLogin.setOnClickListener {
            Log.d("welcome", "login")

//            call.enqueue(object : Callback<MutableList<PostModel>>{
//                override fun onResponse(
//                    call: Call<MutableList<PostModel>>,
//                    response: Response<MutableList<PostModel>>
//                ) {
//                    Log.d("welcome", response.body().toString())
//                }
//
//                override fun onFailure(call: Call<MutableList<PostModel>>, t: Throwable) {
//                    Log.d("welcome", t.message.toString())
//                }
//
//            })
//            call.enqueue(object : Callback<ResponseBody> {
//                override fun onResponse(
//                    call: Call<ResponseBody>,
//                    response: Response<ResponseBody>
//                ) {
//                    Log.d("welcome", response.isSuccessful.toString())
//                    if(response.isSuccessful){
//                        if (response.body() != null) {
//                            val s = response.body()!!.string().toString()
//                            Log.d("welcome", "ok")
//                            Log.d("welcome", s)
//                            Log.d("welcome", response.body().toString())
//                        }else{
//                            Log.d("welcome", "empty")
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                    Log.d("welcome", t.message.toString())
//                }
//            })


//            val call2 = serviceGenerator.getVehicles(bearerToken)
//            call2.enqueue(object : Callback<MutableList<VehicleModel>>{
//                override fun onResponse(
//                    call: Call<MutableList<VehicleModel>>,
//                    response: Response<MutableList<VehicleModel>>
//                ) {
//                    if(response.isSuccessful) {
//                        if (response.body() != null) {
//                            Log.d("welcome2", response.code().toString())
//                            Log.d("welcome2", response.body().toString())
//                        }else {
//                            Log.d("welcome2", "emptyVehicle")
//                        }
//                    }
//                }
//
//                override fun onFailure(
//                    call: Call<MutableList<VehicleModel>>,
//                    t: Throwable
//                ) {
//                    Log.d("welcome2", t.message.toString())
//                }
//
//            })

            if (username.text.toString().isEmpty()) {
                username.requestFocus()
                username.error = "Please enter the username"
            }
            else if (password.text.toString().isEmpty()) {
                password.requestFocus()
                password.error = "Please enter the password"
            }
            // login to server
            else {
                val call = serviceGenerator.postLogin(
                    LoginModel(
                        username = username.text.toString(),
                        password = password.text.toString(),
                    )
                )
                call.enqueue(object : Callback<ResponseBody>{
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if(response.isSuccessful) {
                            if (response.body() != null) {
                                Log.d("welcome", "ok")
                                Log.d("welcome", response.code().toString()) // http status code (200)
                                Log.d("welcome", response.body().toString())
                                Log.d("welcome", response.headers().get("Authorization").toString())
                                gotoHome(response.code())
                                bearerToken = response.headers().get("Authorization").toString()
                            } else {
                                Log.d("welcome", "empty")
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.d("welcome", t.message.toString())
                    }

                })
            }
        }

        // go to Register screen
        btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    fun gotoHome(loginHttpStatus : Int){
        Log.d("welcome", loginHttpStatus.toString())
        if(loginHttpStatus == 200)
            startActivity(Intent(this, HomeActivity::class.java))
        else
            btnLogin.error = "Username or password is incorrect!"
    }
}