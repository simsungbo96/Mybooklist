package com.sbsj.mybooklist.adapter

import com.sbsj.mybooklist.databinding.ItemHistoryBinding
import com.sbsj.mybooklist.model.History
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView



//historyDeleteClickedListener : (String) -> (Unit) 스트링 값을 받지만 리턴을 반환을 람다형식으로 할 경우
class HistoryAdapter(val historyDeleteClickedListener : (String) -> (Unit) , val historyQuickSearch : (String) -> (Unit)) :ListAdapter<History,HistoryAdapter.HistoryItemViewHolder>(diffUtil) {
    //뷰바인딩이란 : ItemBookBiding -> item_book 을 카멜 방식으로 ItemBook + Binding 형태로 만들어짐.
    inner class HistoryItemViewHolder(private val binding : ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(historyModel : History){
            binding.historyKeywordTextView.text = historyModel.keyword

            binding.historyDeleteBtn.setOnClickListener {
                historyDeleteClickedListener(historyModel.keyword.orEmpty())
            }
            binding.historyKeywordTextView.setOnClickListener {
                historyQuickSearch(historyModel.keyword.orEmpty())
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        return HistoryItemViewHolder(ItemHistoryBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        holder.bind(currentList[position])

    }
    //difUtil 이란 리사이클러뷰가 뷰포지션이 바뀔때 값을 할당할것인지 결정해주는 것

    companion object{
        val diffUtil = object :DiffUtil.ItemCallback<History>(){
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem.keyword == newItem.keyword
            }

        }
    }
}