package com.example.incurable.drinks


import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.incurable.drinks.database.DrinkSQLiteHelper
import com.example.incurable.drinks.model.Drink
import com.example.incurable.drinks.services.CaptionedImageAdapter


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class TopFragment : Fragment() {
    inner class DrinkListTask() : AsyncTask<Void, Void, MutableList<Drink>>() {
        override fun doInBackground(vararg p0: Void?): MutableList<Drink> {
            val sqlHelper= DrinkSQLiteHelper(context!!)
            val db=sqlHelper.readableDatabase
            val cursor=db?.query("DRINK", arrayOf("_id","NAME","DESC","IMAGE_URL","FAVORITE"),null,
                null,null,null,null)!!
            var result= mutableListOf<Drink>()
            while (cursor.moveToNext()){
                val id=cursor?.getInt(cursor?.getColumnIndex("_id"))
                val nameIndex=cursor?.getColumnIndex("NAME")
                val name=cursor?.getString(nameIndex)
                val descIndex=cursor?.getColumnIndex("DESC")
                val desc=cursor?.getString(descIndex)
                val picIndex=cursor?.getColumnIndex("IMAGE_URL")
                val pic=cursor?.getString(picIndex)
                val favoriteIndex=cursor?.getColumnIndex("FAVORITE")
                val favorite=cursor?.getInt(favoriteIndex)==1
                val drinkValue=Drink(id,name, desc, pic, favorite)
                result.add(drinkValue)
            }
            return result
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        var drinkRecycler=inflater.inflate(R.layout.fragment_drink_material, container, false) as RecyclerView
        val adapter=CaptionedImageAdapter(DrinkListTask().execute().get())
        adapter.setListener(object : CaptionedImageAdapter.Listener{
            override fun onClick(position: Int) {
                var intent=Intent(activity,DrinkDetailActivity::class.java)
                intent.putExtra(DrinkDetailActivity.drinkNo,position)
                activity?.startActivity(intent)
            }
        })
        drinkRecycler.adapter=adapter
        drinkRecycler.layoutManager=LinearLayoutManager(activity)
        return drinkRecycler
    }


}
