package hu.bme.aut.rentapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import hu.bme.aut.rentapp.R
import hu.bme.aut.rentapp.data.DataManager
import kotlinx.android.synthetic.main.details_first.*

class DetailsFirstFragment : Fragment(R.layout.details_first){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var store = DataManager.store
        size.text = store.size
        nr.text = store.nr
    }
}