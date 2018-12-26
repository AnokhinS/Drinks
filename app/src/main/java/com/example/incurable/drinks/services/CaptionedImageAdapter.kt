package com.example.incurable.drinks.services

import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.example.incurable.drinks.DrinkDetailActivity
import com.example.incurable.drinks.R
import com.example.incurable.drinks.model.Drink
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

class CaptionedImageAdapter(val drinks:List<Drink>): RecyclerView.Adapter<CaptionedImageAdapter.ViewHolder>() {

    inner class ViewHolder(cardView: CardView):RecyclerView.ViewHolder(cardView){
        var card:CardView?=null
        init {
            card=cardView
        }
    }
    interface Listener{
        fun onClick(position: Int)
    }

    private var listener:CaptionedImageAdapter.Listener?=null
    fun setListener(listener: CaptionedImageAdapter.Listener) {
        this.listener=listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardView=LayoutInflater.from(parent.context).inflate(R.layout.card_captioned_image,parent,false) as CardView
        return ViewHolder(cardView)
    }

    override fun getItemCount(): Int {
        return drinks.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cardView=holder.card
        val drink=drinks.get(index = position)
        val imageView=cardView?.findViewById<ImageView>(R.id.icon)
        Picasso.get().load(drink.image_url).into(imageView)
        val nameView=cardView?.findViewById<TextView>(R.id.drink_name)
        nameView?.text=drink.name
        val descView=cardView?.findViewById<TextView>(R.id.desc)
        descView?.text=drink.description
        cardView?.setOnClickListener{
            if (listener!=null){
                listener?.onClick(drink.id)

            }
        }
    }


}