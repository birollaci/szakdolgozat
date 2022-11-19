package hu.bme.aut.rentapp.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import hu.bme.aut.rentapp.R
import hu.bme.aut.rentapp.data.DataManager
import kotlinx.android.synthetic.main.details_second.*

class DetailsSecondFragment : Fragment(R.layout.details_second){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var n: Int = 0
        for (i in DataManager.boxesarray) {
            n++
            boxes.append(n.toString() + ".: " + i[0].toString() + " x " + i[1].toString())
            boxes.append("\n")
        }
    }
}