package com.sbsj.mybooklist.model



import com.google.gson.annotations.SerializedName


data class Book(
    @SerializedName("isbn") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val priceSales: String,
    @SerializedName("image") val coverSmallUrl: String,
    @SerializedName("link") val mobileLink: String
)