package com.example.coffeshop.Activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.coffeshop.Adapter.ItemListCategoryAdapter
import com.example.coffeshop.ViewModel.MainViewModel
import com.example.coffeshop.databinding.ActivityItemsListBinding

class ItemsListActivity : AppCompatActivity() {

    lateinit var binding: ActivityItemsListBinding
    private val viewModel = MainViewModel()
    private var id: String? = ""
    private var title: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityItemsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getBundles()
        initList()
    }

    private fun initList() {
        binding.apply {

            progressBar.visibility = View.VISIBLE

            Log.e("ITEM", "id Category : $id")

            if (id.isNullOrEmpty()) {
                progressBar.visibility = View.GONE
                return
            }

            viewModel.loadItems(id!!).observe(this@ItemsListActivity) {
                Log.e("ITEM", "Jumlah data = ${it.size}") // <- ini juga
                listView.layoutManager =
                    GridLayoutManager(this@ItemsListActivity,2)
                listView.adapter = ItemListCategoryAdapter(it)
                progressBar.visibility = View.GONE
            }

            backBtn.setOnClickListener {
                finish()
            }
        }
    }
    private fun getBundles() {
        id = intent.getStringExtra("id")
        title = intent.getStringExtra("title")

        binding.categoryTxt.text = title
    }
}