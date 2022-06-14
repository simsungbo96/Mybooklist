package com.sbsj.mybooklist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.room.Room
import com.bumptech.glide.Glide
import com.sbsj.mybooklist.databinding.ActivityDetailBinding
import com.sbsj.mybooklist.model.Book
import com.sbsj.mybooklist.model.Review

class DetailActivity : AppCompatActivity() {

    private lateinit var db: AppDataBase
    private lateinit var binding: ActivityDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        db = getAppDatabase(this)


        val bookModel = intent.getParcelableExtra<Book>("bookModel")


        binding.titleTextView.text = bookModel?.title.orEmpty()


        Glide.with(binding.coverImageView.context)
            .load(bookModel?.coverSmallUrl.orEmpty())
            .into(binding.coverImageView)

        binding.descriptionTextView.text = bookModel?.description.orEmpty()

        Thread {
            val review = db.reviewDao().getOne(bookModel!!.id)
            Log.e("TAG", "onCreate: $review")

            runOnUiThread {
                binding.reviewEditText.setText(review?.review.orEmpty())
            }
        }.start()

        binding.saveButton.setOnClickListener {
            Thread {
                db.reviewDao().saveReview(
                    Review(
                        bookModel?.id.orEmpty(),
                        binding.reviewEditText.text.toString()
                    )
                )
            }.start()
        }
    }
}