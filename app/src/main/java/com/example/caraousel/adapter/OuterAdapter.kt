package com.example.caraousel.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caraousel.R
import com.example.caraousel.model.DisplayableChartItem
import com.example.caraousel.model.OuterListItem
import com.example.caraousel.viewmodel.ChartCategory

private const val VIEW_TYPE_CATEGORY = 1
private const val VIEW_TYPE_BANNER = 2

class OuterAdapter(
    private val itemClickListener: (DisplayableChartItem) -> Unit // Listener for INNER items
    // Add bannerClickListener if needed: , private val bannerClickListener: (Long) -> Unit
) : ListAdapter<OuterListItem, RecyclerView.ViewHolder>(OuterDiffCallback()) { // Adapt OuterListItem

    private val viewPool = RecyclerView.RecycledViewPool()

    // --- ViewHolder for Category Rows ---
    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryTitle: TextView = itemView.findViewById(R.id.textViewCategoryTitle)
        private val innerRecyclerView: RecyclerView = itemView.findViewById(R.id.innerChartRecyclerView)
        private var innerAdapter: InnerChartAdapter? = null // Keep reference

        fun bind(categoryItem: ChartCategory, listener: (DisplayableChartItem) -> Unit, viewPool: RecyclerView.RecycledViewPool) {
            categoryTitle.text = categoryItem.title

            // Setup inner RecyclerView only if needed or adapter changes
            if (innerRecyclerView.layoutManager == null) {
                innerRecyclerView.layoutManager = LinearLayoutManager(
                    itemView.context, LinearLayoutManager.HORIZONTAL, false
                ).apply { initialPrefetchItemCount = categoryItem.items.size.coerceAtLeast(4) }
            }
            if (innerRecyclerView.adapter == null || innerAdapter == null) { // Ensure adapter exists
                innerAdapter = InnerChartAdapter(listener) // Create inner adapter instance
                innerRecyclerView.adapter = innerAdapter
                innerRecyclerView.setRecycledViewPool(viewPool)
            }

            // Submit the list to the inner adapter
            innerAdapter?.submitList(categoryItem.items)
        }
    }

    class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val bannerImageView: ImageView = itemView.findViewById(R.id.imageViewOuterBanner)

        fun bind(bannerItem: OuterListItem.BannerAdItem /*, listener: (Long) -> Unit */ ) {

            val context = itemView.context
            val fallbackBannerDrawable= R.drawable.image2

            if (!bannerItem.imageUrl.isNullOrBlank()) {
                // Valid API image URL exists: Load it
                Log.d("BannerViewHolder", "Loading API banner image: ${bannerItem.imageUrl}")
                Glide.with(context)
                    .load(bannerItem.imageUrl)
                    .placeholder(fallbackBannerDrawable) // Use fallback as placeholder
                    .error(fallbackBannerDrawable)       // Use fallback on error too
                    .centerCrop()
                    .into(bannerImageView)
            } else {
                // API image URL is missing: Load the hardcoded fallback drawable
                Log.d("BannerViewHolder", "Loading fallback banner drawable.")
                Glide.with(context)
                    .load(fallbackBannerDrawable) // Load the drawable resource
                    .centerCrop()
                    .into(bannerImageView)
            }
            //itemView.setOnClickListener { listener(bannerItem.id) }
        }
    }


    override fun getItemViewType(position: Int): Int {
        // Determine view type based on the sealed class instance
        return when (getItem(position)) {
            is OuterListItem.CategoryItem -> VIEW_TYPE_CATEGORY
            is OuterListItem.BannerAdItem -> VIEW_TYPE_BANNER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        // Inflate the correct layout based on the view type
        return when (viewType) {
            VIEW_TYPE_CATEGORY -> {
                val view = inflater.inflate(R.layout.item_outer_chart_row, parent, false)
                CategoryViewHolder(view)
            }
            VIEW_TYPE_BANNER -> {
                val view = inflater.inflate(R.layout.item_inner_chart_rectangle, parent, false)
                BannerViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type in OuterChartAdapter")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        // Bind data based on the ViewHolder type
        when (holder) {
            is CategoryViewHolder -> {
                // We know item must be CategoryItem if holder is CategoryViewHolder
                holder.bind((item as OuterListItem.CategoryItem).category, itemClickListener, viewPool)
            }
            is BannerViewHolder -> {
                // We know item must be BannerAdItem if holder is BannerViewHolder
                holder.bind(item as OuterListItem.BannerAdItem /*, bannerClickListener */)
            }
        }
    }

    // --- DiffUtil for Outer List ---
    class OuterDiffCallback : DiffUtil.ItemCallback<OuterListItem>() {
        override fun areItemsTheSame(oldItem: OuterListItem, newItem: OuterListItem): Boolean {
            return when {
                // Compare category titles if both are categories
                oldItem is OuterListItem.CategoryItem && newItem is OuterListItem.CategoryItem ->
                    oldItem.category.title == newItem.category.title
                // Compare banner IDs if both are banners
                oldItem is OuterListItem.BannerAdItem && newItem is OuterListItem.BannerAdItem ->
                    oldItem.id == newItem.id
                // Different types are never the same item
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: OuterListItem, newItem: OuterListItem): Boolean {
            return when {
                oldItem is OuterListItem.CategoryItem && newItem is OuterListItem.CategoryItem ->
                    oldItem.category == newItem.category // Rely on data class equality
                oldItem is OuterListItem.BannerAdItem && newItem is OuterListItem.BannerAdItem ->
                    oldItem == newItem // Rely on data class equality
                else -> false // Different types cannot have same content
            }
        }
    }
}