package com.example.coffeshop.Domain

import android.accessibilityservice.GestureDescription
import java.io.Serializable

data class ItemsModel(
    var title: String = "",
    var description: String="",
    val picUrl: List<String> = emptyList(),
    var price: Double=0.0,
    var rating: Double=0.0,
    var numberInCart: Int=0,
    var extra: String="",
): Serializable

