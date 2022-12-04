package hu.bme.aut.rentapp.activity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import hu.bme.aut.rentapp.R
import hu.bme.aut.rentapp.api.ApiService
import hu.bme.aut.rentapp.data.DataManager
import hu.bme.aut.rentapp.service.ServiceGenerator
import hu.bme.aut.rentapp.databinding.ActivityScanBinding
import hu.bme.aut.rentapp.models.ContractModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ScanActivity : AppCompatActivity() {

    private val requestCodeCameraPermission = 1001
    private lateinit var cameraSource: CameraSource
    private lateinit var barcodeDetector: BarcodeDetector
    private var scannedValue = ""
    private lateinit var binding: ActivityScanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("welcome", "ScanActivity")
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        binding = ActivityScanBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (ContextCompat.checkSelfPermission(this@ScanActivity, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            askForCameraPermission()
        } else {
            setupControls()
        }

        val aniSlide: Animation =
            AnimationUtils.loadAnimation(this@ScanActivity, R.anim.scanner_animation)
        binding.barcodeLine.startAnimation(aniSlide)
    }

    private fun askForCameraPermission() {
        ActivityCompat.requestPermissions(
            this@ScanActivity,
            arrayOf(android.Manifest.permission.CAMERA),
            requestCodeCameraPermission
        )
    }

    private fun setupControls() {
        barcodeDetector =
            BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS).build()

        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setRequestedPreviewSize(1000, 500)
            .setAutoFocusEnabled(true) //you should add this feature
            .build()

        binding.cameraSurfaceView.getHolder().addCallback(object : SurfaceHolder.Callback {
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    //Start preview after 1s delay
                    cameraSource.start(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            @SuppressLint("MissingPermission")
            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                try {
                    cameraSource.start(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })


        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                Toast.makeText(applicationContext, "Scanner has been closed", Toast.LENGTH_LONG)
                    .show()
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() == 1) {
                    scannedValue = barcodes.valueAt(0).rawValue

                    //Don't forget to add this line printing value or finishing activity must run on main thread
                    runOnUiThread {
                        if(isNumeric(scannedValue)) {
                            val id: Long = scannedValue.toLong()
                            addVechicleToContract(id)
                        }else {
                            Toast.makeText(this@ScanActivity, "The scanned value must be a number!", Toast.LENGTH_LONG).show()
                        }
                        cameraSource.stop()
                        finish()
                    }
                }else
                {
                    Toast.makeText(this@ScanActivity, "value- else", Toast.LENGTH_LONG).show()

                }
            }
        })
    }

    fun isNumeric(toCheck: String): Boolean {
        return toCheck.all { char -> char.isDigit() }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupControls()
            } else {
                Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraSource.stop()
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
                addOk(response.code(), id)
            }

            override fun onFailure(call: Call<ContractModel>, t: Throwable) {
                Log.d("welcome error", t.message.toString())
                addOk(404, id)
            }
        })
    }
    private fun addOk(code: Int, id: Long?) {
        when (code) {
            200 -> Toast.makeText(this@ScanActivity, "The item added to contract!", Toast.LENGTH_LONG).show()
            400 -> Toast.makeText(this@ScanActivity, "The item with identifier $id exist in contract!", Toast.LENGTH_LONG).show()
            else -> Toast.makeText(this@ScanActivity, "Scanned value: $id\nScanned identifier not found!", Toast.LENGTH_LONG).show()
        }
    }
}