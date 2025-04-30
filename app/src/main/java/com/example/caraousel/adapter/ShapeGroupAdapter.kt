package com.example.caraousel.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caraousel.databinding.ItemCharacterGroupBinding
import com.example.caraousel.model.Album
import com.example.caraousel.model.ShapeGroup
import com.example.proj.databinding.ItemCharacterGroupBinding
import com.example.proj.model.Character
import com.example.proj.model.ShapeGroup

class ShapeGroupAdapter(
    private var groups: List<ShapeGroup>,
    private val onCharacterClick: (Album) -> Unit
) : RecyclerView.Adapter<ShapeGroupAdapter.GroupViewHolder>() {


    inner class GroupViewHolder(private val binding: ItemCharacterGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val innerRecyclerView = binding.rvInnerCharacters

        private lateinit var innerAdapter: CharacterShapeAdapter

        init {
            innerRecyclerView.layoutManager = LinearLayoutManager(
                binding.root.context, LinearLayoutManager.HORIZONTAL, false
            )
            innerRecyclerView.isNestedScrollingEnabled = false
        }

        fun bind(group: ShapeGroup) {
            binding.tvGroupTitle.text = group.title // Use the title from ShapeGroup

            // Create or update the inner adapter, passing the specific shapeType
            if (!::innerAdapter.isInitialized || innerAdapter.shapeType!= group.shapeType) {
                // *** Pass group.shapeType to the inner adapter ***
                innerAdapter = CharacterShapeAdapter(group.character, group.shapeType, onCharacterClick)
                innerRecyclerView.adapter = innerAdapter
            } else {
                // Update data if the adapter exists and shape is the same (unlikely with this structure but safe)
                innerAdapter.updateData(group.character)
            }
            // Optimization: If you know the inner items might change height based on shape,
            // you might need to request layout update on the ViewHolder's itemView sometimes.
            // itemView.requestLayout()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val binding = ItemCharacterGroupBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return GroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.bind(groups[position])
    }

    override fun getItemCount(): Int = groups.size


    fun updateData(newGroups: List<ShapeGroup>) {
        groups = newGroups
        notifyDataSetChanged()
    }
}