package hu.bme.aut.rentapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import hu.bme.aut.rentapp.R
import hu.bme.aut.rentapp.data.DataManager
import kotlinx.android.synthetic.main.chart.*

class Chart : Fragment(R.layout.chart){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadChartDatas()
    }

    private fun loadChartDatas(){
        var entries: MutableList<PieEntry>
        if(DataManager.empty > 0) {
            entries = mutableListOf(
                    PieEntry(DataManager.empty.toFloat(), "Empty")
            )
            for (i in DataManager.inr) {
                entries.add(PieEntry(i[3].toFloat(),i[2].toString() + ". box"))
            }
            if(DataManager.sum2 > 0)
                entries.add(PieEntry(DataManager.sum2.toFloat(), "Didn't fit"))
        }else {
            entries = mutableListOf()
            for (i in DataManager.boxesarray) {
                entries.add(PieEntry(i[3].toFloat(),i[2].toString() + ". box"))
            }
        }



        val dataSet = PieDataSet(entries, "Storage")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()

        val data = PieData(dataSet)
        chartPayment.data = data
        chartPayment.centerText = "Summary: " + DataManager.size
        chartPayment.setCenterTextSize(14f)
        chartPayment.invalidate()
    }


}