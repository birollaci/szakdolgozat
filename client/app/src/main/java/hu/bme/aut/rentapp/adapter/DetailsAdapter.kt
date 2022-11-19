package hu.bme.aut.rentapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import hu.bme.aut.rentapp.fragments.DetailsSecondFragment
import hu.bme.aut.rentapp.fragments.DetailsFirstFragment
import hu.bme.aut.rentapp.fragments.Chart

class DetailsAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment = when(position){
        0 -> DetailsFirstFragment()
        1 -> DetailsSecondFragment()
        2 -> Chart()
        else -> DetailsFirstFragment()
    }

    override fun getCount() : Int = NUM_PAGES

    companion object{
        const val NUM_PAGES = 3
    }
}