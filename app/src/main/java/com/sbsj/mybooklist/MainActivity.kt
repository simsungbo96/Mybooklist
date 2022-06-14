package com.sbsj.mybooklist

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Database
import androidx.room.Room
import com.sbsj.mybooklist.adapter.BookAdapter
import com.sbsj.mybooklist.adapter.HistoryAdapter
import com.sbsj.mybooklist.api.BookService
import com.sbsj.mybooklist.api.Retrofit2Class
import com.sbsj.mybooklist.databinding.ActivityMainBinding
import com.sbsj.mybooklist.model.Book
import com.sbsj.mybooklist.model.History
import com.sbsj.mybooklist.model.SearchBooksDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: BookAdapter
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var bookService: BookService

    private lateinit var db: AppDataBase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // 액티비티안에는 layoutInflater가 이미 있음.

        setContentView(binding.root)
        initBookRecyclerView()
        initHistroyRecyclerView()

        db = getAppDatabase(this)

        val retrofit = Retrofit2Class().retrofit
        bookService = retrofit.create(BookService::class.java)


    }

    private fun search(keyword: String) {
        bookService.getBooksByName(
            getString(R.string.naver_id_key),
            getString(R.string.naver_secret_key),
            keyword
        )
            .enqueue(object : Callback<SearchBooksDto> {
                override fun onResponse(
                    call: Call<SearchBooksDto>,
                    response: Response<SearchBooksDto>
                ) {
                    // todo 응답코드가 200이아니고 400이라도 호출하기 떄문에 if문에 적용해야함.
                    hideHistoryView()
                    saveSearchKeyword(keyword)
                    if (response.isSuccessful.not()) {
                        Log.d(TAG, "not success${response}")
                        return
                    }
                    response.body()?.let {
                        adapter.submitList(response.body()?.books.orEmpty()) //orempty 할경우 []비어있는 리스트 넣어줌.
                    }
                }

                override fun onFailure(call: Call<SearchBooksDto>, t: Throwable) {
                    // todo 실패할 경우
                    hideHistoryView()
                }

            })
    }

    private fun showHistoryView() {
        Thread {
            val keywords = db.historyDao().getAll().reversed() // 최신순으로
            Log.e(TAG, "showHistoryView: $keywords")
            runOnUiThread {
                binding.historyRecyclerView.isVisible = true
                historyAdapter.submitList(keywords.orEmpty())
            }
        }.start()
        binding.historyRecyclerView.isVisible = true
    }

    private fun hideHistoryView() {
        binding.historyRecyclerView.isVisible = false
    }


    private fun initBookRecyclerView() {
        adapter = BookAdapter(itemClickedListener = {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("bookModel", it) //직렬화처리를 했기때문에 모델전체를 보낼수있음.
            startActivity(intent)
        })
        binding.bookRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.bookRecyclerView.adapter = adapter
    }

    private fun initHistroyRecyclerView() {
        historyAdapter = HistoryAdapter(historyDeleteClickedListener = {
            deleteSearchKeyword(it)
        }, historyQuickSearch = {
            search(keyword = it)
        })

        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.historyRecyclerView.adapter = historyAdapter
        initSearchEditText()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun initSearchEditText() {
        binding.searchEditText.setOnKeyListener { view, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == MotionEvent.ACTION_DOWN) {
                search(binding.searchEditText.text.toString())
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        binding.searchEditText.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                showHistoryView()
            }
            return@setOnTouchListener false
        }
    }


    private fun deleteSearchKeyword(keyword: String) {
        Thread {
            db.historyDao().delete(keyword)
            showHistoryView()
        }.start()
    }

    private fun saveSearchKeyword(keyword: String) {
        Thread {
            db.historyDao().insertHistory(History(null, keyword))
        }.start()
    }


    companion object {
        const val TAG = "MainActivity"

    }
}