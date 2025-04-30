package com.example.caraousel.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caraousel.R
import com.example.caraousel.databinding.CarasouelItemBinding
import com.example.caraousel.model.Album

class CarouselAdapter(
    private var albums: List<Album>,
    private val onItemClick:(Album)-> Unit
    ) : RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    inner class CarouselViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        val image:ImageView = view.findViewById(R.id.carousel_image_view)

        init {
            view.setOnClickListener {
                onItemClick(albums[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val view=  LayoutInflater.from(parent.context).inflate(R.layout.carasouel_item,parent,false)
        return CarouselViewHolder(view)
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {

        val context = holder.itemView.context

        holder.itemView.scaleX = 0.20f
        holder.itemView.scaleY = 0.20f
        holder.itemView.animate()
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(300)
            .setInterpolator(OvershootInterpolator())
            .start()

        Glide.with(context)
            .load(albums[position].cover_medium)
            .placeholder(R.drawable.image)
            .into(holder.image)
    }

    fun updateList(newAlbums: List<Album>) {
        albums = newAlbums
        notifyDataSetChanged()
    }
}