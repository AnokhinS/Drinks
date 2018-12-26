package com.example.incurable.drinks.model

data class Drink(val id:Int,var name:String,var description:String,var image_url:String,var favorite:Boolean?=null) {
}