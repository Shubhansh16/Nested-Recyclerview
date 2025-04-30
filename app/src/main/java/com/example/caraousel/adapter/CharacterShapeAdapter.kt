package com.example.caraousel.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caraousel.R
import com.example.caraousel.ShapeType
import com.example.caraousel.model.Album

class CharacterShapeAdapter(
    private var characters: List<Album>,
    val shapeType: ShapeType,
    private val onCharacterClick: (Album) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    inner class CircleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.ivCharacter)
        private val nameView: TextView = itemView.findViewById(R.id.tvCharacterName)

        fun bind(character: Album) {
            nameView.text = (character.title ?: character.id ?: "Unknown").toString()
            Glide.with(itemView.context)
                .load(character.cover_medium)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .circleCrop()
                .into(imageView)
            itemView.setOnClickListener { onCharacterClick(character) }
        }
    }

    inner class SquareViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.ivCharacter)
        private val nameView: TextView = itemView.findViewById(R.id.tvCharacterName)

        fun bind(character: Album) {
            nameView.text = (character.title ?: character.id ?: "Unknown").toString()
            Glide.with(itemView.context)
                .load(character.cover_medium)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .centerCrop()
                .into(imageView)
            itemView.setOnClickListener { onCharacterClick(character) }
        }
    }

    inner class RectangleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.ivCharacter)
        private val nameView: TextView = itemView.findViewById(R.id.tvCharacterName)

        fun bind(character: Album) {
            nameView.text = (character.title ?: character.id ?: "Unknown").toString()
            Glide.with(itemView.context)
                .load(character.cover_medium)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .centerCrop()
                .into(imageView)
            itemView.setOnClickListener { onCharacterClick(character) }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (shapeType) {
            ShapeType.CIRCLE -> {
                val view = inflater.inflate(R.layout.item_character_circle, parent, false)
                CircleViewHolder(view)
            }
            ShapeType.SQUARE -> {
                val view = inflater.inflate(R.layout.item_character_square, parent, false)
                SquareViewHolder(view)
            }
            ShapeType.RECTANGLE -> {
                val view = inflater.inflate(R.layout.item_character_rectangle, parent, false)
                RectangleViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val character = characters[position]

        when (holder) {
            is CircleViewHolder -> holder.bind(character)
            is SquareViewHolder -> holder.bind(character)
            is RectangleViewHolder -> holder.bind(character)

            else -> System.err.println("Unexpected ViewHolder type in CharacterShapeAdapter")
        }
    }

    override fun getItemCount(): Int = characters.size

    fun updateData(newCharacters: List<Album>) {
        characters = newCharacters
        notifyDataSetChanged()
    }
}