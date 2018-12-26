package com.example.incurable.drinks.services

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import com.example.incurable.drinks.database.DrinkSQLiteHelper
import com.example.incurable.drinks.model.Drink
import khttp.responses.Response

class MyContentProvider : ContentProvider() {
    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun query(p0: Uri, p1: Array<String>?, p2: String?, p3: Array<String>?, p4: String?): Cursor? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getType(p0: Uri): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    inner class DrinkListTask() : AsyncTask<Void, Void, MutableList<Drink>>() {
        override fun doInBackground(vararg p0: Void?): MutableList<Drink> {
            var result= mutableListOf<Drink>()
            val response: Response = khttp.get("https://www.thecocktaildb.com/api/json/v1/1/search.php?s=margarita")
            var drinks = response.jsonObject.getJSONArray("drinks")
            for (i in 0 until drinks.length()){
                val jsonDrink=drinks.getJSONObject(i)
                var id=jsonDrink.get("idDrink").toString().toInt()
                var name=jsonDrink.get("strDrink")as String
                var desc=jsonDrink.get("strInstructions")as String
                var image=jsonDrink.get("strDrinkThumb")as String
                val sqlHelper= DrinkSQLiteHelper(context!!)
                val db=sqlHelper.readableDatabase
                val cursor=db?.query("DRINK", arrayOf("_id","NAME","DESC","IMAGE_URL","FAVORITE"),"_id=?",
                    arrayOf("$id"),null,null,null)!!

                cursor.moveToFirst()
                val favoriteIndex=cursor?.getColumnIndex("FAVORITE")
                var isFavorite=false
                if (cursor?.count>0){
                    isFavorite=cursor?.getInt(favoriteIndex)==1
                }
                val drinkValue= Drink(id,name,desc,image,isFavorite)
                result.add(drinkValue)
            }
            return result
        }
    }

    fun getListFromUrl():MutableList<Drink>{
        return DrinkListTask().execute().get()
    }

}