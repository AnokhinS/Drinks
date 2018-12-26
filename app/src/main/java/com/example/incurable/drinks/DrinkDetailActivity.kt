package com.example.incurable.drinks

import android.content.ContentValues
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.example.incurable.drinks.database.DrinkSQLiteHelper
import com.example.incurable.drinks.model.Drink
import com.squareup.picasso.Picasso
import khttp.responses.Response

class DrinkDetailActivity : AppCompatActivity() {
    companion object {
        val drinkNo = "drinkNo"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drink_detail)
        DrinkDetailTask().execute()
    }

    private fun addTextToView(id:Int,text:String){
        findViewById<TextView>(id).text=text
    }

    fun onFavoriteClicked(view:View){
        if(FavoriteTask().execute().get()){
            Log.e("Insert","Added")
        }else{
            Log.e("Insert","Removed")
        }
    }

    inner class FavoriteTask() : AsyncTask<Void, Void, Boolean>() {
        var drinkValue=ContentValues()
        override fun onPreExecute() {
            super.onPreExecute()
            val checkBox=findViewById<CheckBox>(R.id.favorite_checkbox)
            drinkValue.put("FAVORITE",checkBox.isChecked)
        }

        override fun doInBackground(vararg p0: Void?): Boolean {
            val index = intent.extras.getInt(drinkNo)
            val sqlHelper= DrinkSQLiteHelper(this@DrinkDetailActivity)
            val db=sqlHelper.writableDatabase
            val cursor = db?.query(
                "DRINK", arrayOf("_id", "NAME", "DESC", "IMAGE_URL", "FAVORITE"), "_id=?",
                arrayOf("$index"), null, null, null)!!
            cursor.moveToFirst()
            if (cursor?.count > 0) {
                db.delete("DRINK","_id=?", arrayOf("$index"))
            }else{
                val response: Response = khttp.get("https://www.thecocktaildb.com/api/json/v1/1/search.php?s=margarita")
                var drinks = response.jsonObject.getJSONArray("drinks")
                for (i in 0 until drinks.length()) {
                    val jsonDrink = drinks.getJSONObject(i)
                    var id = jsonDrink.get("idDrink").toString().toInt()
                    if (id==index){
                        var name = jsonDrink.get("strDrink") as String
                        var desc = jsonDrink.get("strInstructions") as String
                        var image = jsonDrink.get("strDrinkThumb") as String
                        drinkValue.put("_ID",id)
                        drinkValue.put("NAME",name)
                        drinkValue.put("DESC",desc)
                        drinkValue.put("IMAGE_URL",image)
                        db.insert("DRINK",null,drinkValue)
                        return true
                    }
                }
            }
            return false
        }
    }


    inner class DrinkDetailTask() : AsyncTask<Void, Void, Drink?>() {
        override fun doInBackground(vararg p0: Void?): Drink? {
            val index = intent.extras.getInt(drinkNo)
            val response: Response = khttp.get("https://www.thecocktaildb.com/api/json/v1/1/search.php?s=margarita")
            var drinks = response.jsonObject.getJSONArray("drinks")
            for (i in 0 until drinks.length()) {
                val jsonDrink = drinks.getJSONObject(i)
                var id = jsonDrink.get("idDrink").toString().toInt()
                if (id==index){
                    var name = jsonDrink.get("strDrink") as String
                    var desc = jsonDrink.get("strInstructions") as String
                    var image = jsonDrink.get("strDrinkThumb") as String
                    val sqlHelper = DrinkSQLiteHelper(this@DrinkDetailActivity)
                    val db = sqlHelper.readableDatabase
                    val cursor = db?.query(
                        "DRINK", arrayOf("_id", "NAME", "DESC", "IMAGE_URL", "FAVORITE"), "_id=?",
                        arrayOf("$id"), null, null, null)!!

                    cursor.moveToFirst()
                    val favoriteIndex = cursor?.getColumnIndex("FAVORITE")
                    var isFavorite = false
                    if (cursor?.count > 0) {
                        isFavorite = cursor?.getInt(favoriteIndex) == 1
                    }
                    val drinkValue = Drink(id, name, desc, image, isFavorite)
                    return drinkValue
                }
            }
            return null
        }

        override fun onPostExecute(result: Drink?) {
            super.onPostExecute(result)
            if (result!=null){
                addTextToView(R.id.name,result?.name)
                addTextToView(R.id.desc,result?.description)
                Picasso.get().load(result.image_url).into(findViewById<ImageView>(R.id.icon))
                var checkBox= findViewById<CheckBox>(R.id.favorite_checkbox)
                checkBox.isChecked=result.favorite==true
                checkBox.setOnClickListener { v->
                    onFavoriteClicked(v)
                }

            }
            else{
                addTextToView(R.id.name,"Some error happened")
            }

        }

    }



}
