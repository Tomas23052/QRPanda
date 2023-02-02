package com.dam.QRPanda.activities


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dam.QRPanda.activities.fragments.Fragment1
import com.dam.QRPanda.activities.fragments.Fragment2

//Código tirado da documentação fornecida pelo docente para criar fragmentos
class Frag(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> return Fragment1()
            1 -> return Fragment2()
            else -> return Fragment1()
        }
    }

}