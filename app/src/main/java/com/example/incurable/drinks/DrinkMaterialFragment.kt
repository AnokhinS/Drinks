package com.example.incurable.drinks


import android.content.ContentProvider
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.example.incurable.drinks.database.DrinkSQLiteHelper
import com.example.incurable.drinks.model.Drink
import com.example.incurable.drinks.services.CaptionedImageAdapter
import com.example.incurable.drinks.services.MyContentProvider
import khttp.get
import khttp.responses.Response


/**
 * A simple [Fragment] subclass.
 *
 */
class DrinkMaterialFragment : Fragment() {
    val contentProvider=MyContentProvider()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var drinkRecycler=inflater.inflate(R.layout.fragment_drink_material, container, false) as RecyclerView
        val adapter=CaptionedImageAdapter(contentProvider.getListFromUrl())
        adapter.setListener(object : CaptionedImageAdapter.Listener{
            override fun onClick(position: Int) {
                var intent= Intent(activity,DrinkDetailActivity::class.java)
                intent.putExtra(DrinkDetailActivity.drinkNo,position)
                activity?.startActivity(intent)
            }
        })
        drinkRecycler.adapter=adapter
        drinkRecycler.layoutManager=LinearLayoutManager(activity)
        return drinkRecycler
    }


}
