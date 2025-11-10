package com.example.coffeshop.Adapter

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeshop.Activity.ItemsListActivity
import com.example.coffeshop.Domain.CategoryModel
import com.example.coffeshop.R
import com.example.coffeshop.databinding.ViewholderCategoryBinding

class CategoryAdapter(
    private val items: MutableList<CategoryModel>
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private lateinit var context: Context
    private var selectedPosition = -1
    private var lastSelectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = ViewholderCategoryBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.titleCat.text = item.title

        // Atur tampilan kategori terpilih
        if (selectedPosition == position) {
            holder.binding.titleCat.setBackgroundResource(R.drawable.brown_full_corner_bg)
            holder.binding.titleCat.setTextColor(ContextCompat.getColor(context, R.color.white))
        } else {
            holder.binding.titleCat.setBackgroundResource(R.drawable.white_full_corner_bg)
            holder.binding.titleCat.setTextColor(ContextCompat.getColor(context, R.color.darkbrown))
        }

        holder.binding.root.setOnClickListener {
            lastSelectedPosition = selectedPosition
            selectedPosition = position

            // Update tampilan item sebelumnya dan yang baru dipilih
            if (lastSelectedPosition >= 0) notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)

            // Navigasi ke ItemsListActivity (delay untuk efek klik)
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(context, ItemsListActivity::class.java)
                // Gunakan 'item' (bukan 'category') — pastikan CategoryModel punya field 'id' & 'title'
                intent.putExtra("id", item.id ?: "")        // ID kategori harus dikirim
                intent.putExtra("title", item.title ?: "")  // Nama kategori, misal "Cappuccino"
                context.startActivity(intent)
            }, 200) // 200ms delay agar animasi klik terasa halus
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(val binding: ViewholderCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)
}
