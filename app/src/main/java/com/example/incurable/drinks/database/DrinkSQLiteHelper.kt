package com.example.incurable.drinks.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.incurable.drinks.DrinkDetailActivity
import com.example.incurable.drinks.R

class DrinkSQLiteHelper(context: Context): SQLiteOpenHelper(context, DB_NAME,null, DB_VERSION)  {
    companion object {
        val DB_NAME="drinks"
        val DB_VERSION=1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE DRINK(" +
                "_id TEXT PRIMARY KEY," +
                "NAME TEXT NOT NULL," +
                "DESC TEXT NOT NULL," +
                "IMAGE_URL TEXT," +
                "FAVORITE INTEGER DEFAULT 0);")
//        insertDrink(db,"Latte","Latte desc", R.drawable.latte)
//        insertDrink(db,"Cappucino","Cappucino desc", R.drawable.capp)
//        insertDrink(db,"Filter","Filter desc", R.drawable.filter)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun insertDrink(db:SQLiteDatabase?,name:String,description: String, resourceId:Int){
        var drinkValue=ContentValues()
        drinkValue.put("NAME",name)
        drinkValue.put("DESC",description)
        drinkValue.put("IMAGE_ID",resourceId)
        db?.insert("DRINK",null,drinkValue)
    }
}