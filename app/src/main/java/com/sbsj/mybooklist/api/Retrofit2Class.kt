package com.sbsj.mybooklist.api

import androidx.recyclerview.widget.DiffUtil
import com.sbsj.mybooklist.model.Book
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Retrofit2Class {
    companion object{
        val BASE_URL ="https://openapi.naver.com"
    }
     val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

}



