package com.example.coffeshop.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.coffeshop.Domain.BannerModel
import com.example.coffeshop.Domain.CategoryModel
import com.example.coffeshop.Domain.ItemsModel
import com.example.coffeshop.Repository.MainRepository

class MainViewModel: ViewModel() {

    private val repository: MainRepository by lazy { MainRepository() }

    fun loadBanner(): LiveData<MutableList<BannerModel>> {
        return repository.loadBanner()
    }

    fun loadCategory(): LiveData<MutableList<CategoryModel>> {
        return repository.loadCategory()
    }

    fun loadPopular(): LiveData<MutableList<ItemsModel>> {
        return repository.loadPopular()
    }

    /**
     * Aman terhadap id null / kosong.
     * Mengembalikan LiveData kosong jika id null sehingga caller tidak crash.
     */
    fun loadItems(id: String?): LiveData<MutableList<ItemsModel>> {
        if (id.isNullOrBlank()) {
            val empty = MutableLiveData<MutableList<ItemsModel>>().apply { value = mutableListOf() }
            return empty
        }
        return repository.loadItemCategory(id)
    }
}