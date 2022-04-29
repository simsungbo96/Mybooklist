package com.sbsj.mybooklist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.sbsj.mybooklist.api.BookService
import com.sbsj.mybooklist.api.Retrofit2Class
import com.sbsj.mybooklist.model.Book
import com.sbsj.mybooklist.model.SearchBooksDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


       val retrofit = Retrofit2Class.retrofit


        val bookService = retrofit.create(BookService::class.java)
        bookService.getBooksByName(NAVER_CLIENT_ID,NAVER_SECRET_KEY,"가나")
            .enqueue(object :Callback<SearchBooksDto>{
                override fun onResponse(
                    call: Call<SearchBooksDto>,
                    response: Response<SearchBooksDto>
                ) {
                   // todo 응답코드가 200이아니고 400이라도 호출하기 떄문에 if문에 적용해야함.
                    if(response.isSuccessful.not()){
                        Log.d(TAG,"not success${response}")
                        return
                    }
                    response.body()?.let {
                        Log.d(TAG, it.toString())

                        it.books.forEach {
                            book ->
                            Log.d(TAG, book.toString())
                        }
                    }

                }

                override fun onFailure(call: Call<SearchBooksDto>, t: Throwable) {
                    // todo 실패할 경우

                    Log.d(TAG,t.toString())
                }

            })

    }

    companion object{
        const val TAG = "MainActivity"
        const val NAVER_CLIENT_ID = "rkWKdd232...rlO4iBpJtvkZnHv7tVKa"
        const val NAVER_SECRET_KEY = "rmfotjrmeos2ms1vs ..Ajx8vCnqcu"
    }
}