package com.sbsj.mybooklist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sbsj.mybooklist.databinding.ItemBookBinding
import com.sbsj.mybooklist.model.Book

//ListAdapter vs RecyclerView.Adapter -> ListAdapter 를 사용하면 AsyncListDiffer 사용할 필요 없이 백그라운드 스레드에서 비교연산가능 .

class BookAdapter(private val itemClickedListener: (Book) -> Unit) :
    ListAdapter<Book, BookAdapter.BookItemViewHolder>(diffUtil) {
    //뷰바인딩이란 : ItemBookBiding -> item_book 을 카멜 방식으로 ItemBook + Binding 형태로 만들어짐.
    inner class BookItemViewHolder(private val binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bookModel: Book) {
            binding.titleTextView.text = bookModel.title
            binding.descriptionTextView.text = bookModel.description
            binding.root.setOnClickListener {
                itemClickedListener(bookModel)
            }
            Glide
                .with(binding.coverImageView.context)
                .load(bookModel.coverSmallUrl)
                .into(binding.coverImageView)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookItemViewHolder {
        return BookItemViewHolder(
            ItemBookBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BookItemViewHolder, position: Int) {
        holder.bind(currentList[position])

    }
    //difUtil 이란 리사이클러뷰가 뷰포지션이 바뀔때 값을 할당할것인지 결정해주는 것

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Book>() {
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}