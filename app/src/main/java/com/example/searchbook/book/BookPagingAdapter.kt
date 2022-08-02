package com.example.searchbook.book

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.searchbook.databinding.ViewholderPagingBookItemBinding
import com.example.searchbook.databinding.ViewholderSearchBarBinding
import com.example.searchbook.domain.BookUiModel
import java.util.*


internal class BookPagingAdapter(
    private val listener: SearchBookListener
) : PagingDataAdapter<BookUiModel, BookVH>(object : DiffUtil.ItemCallback<BookUiModel>() {
    override fun areItemsTheSame(oldItem: BookUiModel, newItem: BookUiModel): Boolean {
        if (oldItem is BookUiModel.Book && newItem is BookUiModel.Book) return oldItem.isbn == newItem.isbn
        else {
            return oldItem == newItem
        }
    }

    override fun areContentsTheSame(oldItem: BookUiModel, newItem: BookUiModel): Boolean {
        return oldItem == newItem
    }

}) {
    companion object {
        const val VIEW_TYPE_SEARCH_BAR = 0
        const val VIEW_TYPE_ITEM = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is BookUiModel.Header -> VIEW_TYPE_SEARCH_BAR
            is BookUiModel.Book -> VIEW_TYPE_ITEM
            else -> throw IllegalStateException("Unknown view")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookVH {
        when (viewType) {
            VIEW_TYPE_SEARCH_BAR -> {
                val binding = ViewholderSearchBarBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return BookSearchBarVH(binding, listener)
            }
            VIEW_TYPE_ITEM -> {
                val binding = ViewholderPagingBookItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return BookListItemVH(binding, listener)
            }
            else -> throw IllegalStateException("Do not valid item type")
        }
    }

    override fun onBindViewHolder(holder: BookVH, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

}

abstract class BookVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: BookUiModel)
}

class BookSearchBarVH(
    val binding: ViewholderSearchBarBinding,
    val listener: SearchBookListener
) : BookVH(binding.root) {
    override fun bind(item: BookUiModel) {
        binding.etSearch.addTextChangedListener(
            object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                private var timer = Timer()
                private val DELAY: Long = 1000 // Milliseconds
                override fun afterTextChanged(s: Editable) {
                    timer.cancel()
                    timer = Timer()
                    timer.schedule(
                        object : TimerTask() {
                            override fun run() {
                                listener.onSearch(s.toString().trim())
                            }
                        },
                        DELAY
                    )
                }
            }
        )
    }
}

class BookListItemVH(
    val binding: ViewholderPagingBookItemBinding,
    val listener: SearchBookListener
) : BookVH(binding.root) {
    override fun bind(item: BookUiModel) {
        val book = item as BookUiModel.Book
        with(binding) {
            title.text = book.title
            author.text = "${book.author} 지음"
            itemView.setOnClickListener { listener.onClick(book.isbn) }
        }
    }
}

interface SearchBookListener {
    fun onSearch(query: String)
    fun onClick(isbn: Long)
}