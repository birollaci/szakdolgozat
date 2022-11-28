package hu.bme.aut.rentapp

import android.content.Context
import android.content.DialogInterface
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class Dialog {

    fun showDefaultDialog(context: Context, str: String, title: String) {
        val alertDialog = AlertDialog.Builder(context)

        alertDialog.apply {
            if(title == "Alert")
                setIcon(R.drawable.danger_sign)
            if(title == "Info")
                setIcon(R.drawable.info)
            setTitle(title)
            setMessage(str)
            setPositiveButton("Close") { _: DialogInterface?, _: Int ->
//                Toast.makeText(context, "All right", Toast.LENGTH_SHORT).show()
            }

        }.create().show()
    }
}