package com.example.caraousel.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caraousel.R
import com.example.caraousel.model.DisplayableChartItem

private const val VIEW_TYPE_CIRCLE = 1
private const val VIEW_TYPE_SQUARE = 2

class InnerChartAdapter(
    private val listener:(DisplayableChartItem) ->Unit
) : ListAdapter<DisplayableChartItem, InnerChartAdapter.InnerViewHolder>(DiffCallback()){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InnerChartAdapter.InnerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layoutId = when(viewType){
            VIEW_TYPE_CIRCLE -> R.layout.item_inner_chart_circle
            VIEW_TYPE_SQUARE -> R.layout.item_inner_chart_square
            else -> throw IllegalArgumentException("Failed to load data")
        }

        val view = inflater.inflate(layoutId,parent,false)
        return InnerViewHolder(view)

    }

    override fun onBindViewHolder(holder: InnerChartAdapter.InnerViewHolder, position: Int) {

        val item = getItem(position)
        holder.bind(item,listener)
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isCircle) VIEW_TYPE_CIRCLE else VIEW_TYPE_SQUARE
    }

    class InnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageViewChartItem)
        private val nameView: TextView = itemView.findViewById(R.id.textViewChartItemName)

        fun bind(item: DisplayableChartItem, listener: (DisplayableChartItem) -> Unit) {
            nameView.text = item.title
            Glide.with(itemView.context)
                .load(item.imageUrl)
                .placeholder(R.drawable.image)
                .error(R.drawable.image)
                .centerCrop()
                .into(imageView)

            itemView.setOnClickListener { listener(item) }


            //itemView.animate().cancel()

            // 3. Apply the animation (reset initial state and start new animation)
            itemView.scaleX = 0.30f // Start small
            itemView.scaleY = 0.30f
            itemView.animate()
                .scaleX(1f) // Scale to full size
                .scaleY(1f)
                .setDuration(300) // Animation duration
                .setInterpolator(OvershootInterpolator()) // Apply overshoot effect
                .start() // Start the animation
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<DisplayableChartItem>() {
        override fun areItemsTheSame(oldItem: DisplayableChartItem, newItem: DisplayableChartItem): Boolean {
            return oldItem.id == newItem.id && oldItem.itemType == newItem.itemType
        }
        override fun areContentsTheSame(oldItem: DisplayableChartItem, newItem: DisplayableChartItem): Boolean {
            return oldItem == newItem
        }
    }
}