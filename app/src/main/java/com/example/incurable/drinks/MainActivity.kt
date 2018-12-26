package com.example.incurable.drinks

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.ArrayAdapter
import android.widget.ListView

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val titles=resources.getStringArray(R.array.titles)
        val drawerList=findViewById<ListView>(R.id.drawer)
        drawerList.adapter=ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1,titles)
        drawerList.setOnItemClickListener { adapterView, view, position, id ->
            selectItem(position)
            findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(Gravity.START)
        }
        selectItem(0)
    }

    fun selectItem(position: Int){
        var fragment:Fragment?=null
        when(position){
            0->fragment=DrinkMaterialFragment()
            else->fragment=TopFragment()
        }
        val ft=supportFragmentManager.beginTransaction()
        ft.replace(R.id.content_frame,fragment)
        ft.addToBackStack(null)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ft.commit()
    }




}
