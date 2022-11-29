package hu.bme.aut.rentapp

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.rentapp.data.DataManager
import hu.bme.aut.rentapp.data.ServiceGenerator
import hu.bme.aut.rentapp.models.LoginModel
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class LoginActivity : AppCompatActivity() {

    private val dialog: Dialog = Dialog()

    val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("welcome", "LoginActivity")
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        btnLogin.setOnClickListener {
            login()
        }

        btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btnTestConnection.setOnClickListener {
            welcome()
        }
    }

    private fun login() {
        Log.d("welcome", "login")

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
                            gotoHome(response.code())
                            DataManager.bearerToken = response.headers().get("Authorization").toString()
                            DataManager.profileNameText = username.text.toString()
                        } else {
                            Log.d("welcome", "empty")
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("welcomeError", t.message.toString())
                }

            })
        }
    }

    private fun welcome() {
        val welcomeCall = serviceGenerator.getWelcome()

        welcomeCall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                Log.d("welcome", response.isSuccessful.toString())
                if(response.isSuccessful){
                    if (response.body() != null) {
                        val s = response.body()!!.string().toString()
                        Log.d("welcome", "ok")
                        Log.d("welcome", s)
                        Log.d("welcome", response.body().toString())
                        connectionOk(response.code())
                    }else{
                        Log.d("welcome", "empty")
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("welcome", t.message.toString())
                connectionOk(404)
            }
        })
    }

    fun gotoHome(loginHttpStatus : Int){
        Log.d("welcome", loginHttpStatus.toString())
        if(loginHttpStatus == 200)
            startActivity(Intent(this, HomeActivity::class.java))
        else
            btnLogin.error = "Username or password is incorrect!"
    }

    private fun connectionOk(code: Int) {
        if(code == 200)
            dialog.showDefaultDialog(this, "The connection to the server was successful!", "Info")
        else
            dialog.showDefaultDialog(this, "Failed connection to server!", "Alert")
    }
}