package com.example.searchbook.book

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.searchbook.R
import com.example.searchbook.databinding.ViewholderDateSeparatorBinding
import com.example.searchbook.databinding.ViewholderPagingBookItemBinding
import com.example.searchbook.databinding.ViewholderSearchBarBinding
import com.example.searchbook.domain.BookUiModel
import java.util.*


internal class BookPagingAdapter(
    private val listener: SearchBookListener
) : PagingDataAdapter<BookUiModel, BookVH>(object : DiffUtil.ItemCallback<BookUiModel>() {
    override fun areItemsTheSame(oldItem: BookUiModel, newItem: BookUiModel): Boolean {
        if (oldItem is BookUiModel.SearchBar && newItem is BookUiModel.SearchBar) {
            Log.d("test", "BookUiModel.SearchBar oldItem : $oldItem newItem : $newItem")
            return true
        }
        else if (oldItem is BookUiModel.Book && newItem is BookUiModel.Book) {
            Log.d("test", "BookUiModel.Book oldItem : $oldItem newItem : $newItem")
            return oldItem.isbn == newItem.isbn
        }
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
        const val VIEW_TYPE_DATE_SEPARATOR = 2
    }

    fun isQueryEmpty(): Boolean {
        return (getItem(0) as BookUiModel.SearchBar).query.isEmpty()
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is BookUiModel.SearchBar -> VIEW_TYPE_SEARCH_BAR
            is BookUiModel.Book -> VIEW_TYPE_ITEM
            is BookUiModel.DateSeparator -> VIEW_TYPE_DATE_SEPARATOR
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
            VIEW_TYPE_DATE_SEPARATOR -> {
                val binding = ViewholderDateSeparatorBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return BookDateSeparatorVH(binding)
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
}

abstract class BookVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: BookUiModel)
}

class BookSearchBarVH(
    private val binding: ViewholderSearchBarBinding,
    val listener: SearchBookListener
) : BookVH(binding.root) {
    override fun bind(item: BookUiModel) {
        val model = item as BookUiModel.SearchBar
        with(binding.etSearch) {
            setText(model.query)
            setSelection(model.query.length)
            addTextChangedListener(
                object : TextWatcher {
                    private var timer: Timer? = null
                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    private val DELAY: Long = 1000 // Milliseconds
                    override fun afterTextChanged(s: Editable) {
                        timer?.cancel()
                        timer = Timer()
                        timer?.schedule(
                            object : TimerTask() {
                                override fun run() {
                                    val query = s.toString().trim()
                                    listener.onSearch(query)
                                }
                            },
                            DELAY
                        )
                    }
                }
            )
        }
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
            author.text = itemView.resources.getString(R.string.written_by, book.author)
            itemView.setOnClickListener { listener.onClick(book) }
        }
    }
}

class BookDateSeparatorVH(
    private val binding: ViewholderDateSeparatorBinding
) : BookVH(binding.root) {
    override fun bind(item: BookUiModel) {
        val model = item as BookUiModel.DateSeparator
        with(binding) {
            date.text = itemView.resources.getString(R.string.year, model.date)
        }
    }
}

interface SearchBookListener {
    fun onSearch(query: String)
    fun onClick(book: BookUiModel.Book)
}