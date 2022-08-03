package com.example.searchbook.book

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.searchbook.R
import com.example.searchbook.databinding.ViewholderPagingBookItemBinding
import com.example.searchbook.domain.BookUiModel

internal class BookMarkingBookAdapter(
    private val listener: BookMarkingBookClickListener
) : ListAdapter<BookUiModel.Book, BookMarkingBookVH>(object :
    DiffUtil.ItemCallback<BookUiModel.Book>() {
    override fun areItemsTheSame(oldItem: BookUiModel.Book, newItem: BookUiModel.Book): Boolean {
        return oldItem.isbn == newItem.isbn
    }

    override fun areContentsTheSame(oldItem: BookUiModel.Book, newItem: BookUiModel.Book): Boolean {
        return oldItem == newItem
    }
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookMarkingBookVH {
        val binding = ViewholderPagingBookItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookMarkingBookVH(binding, listener)
    }

    override fun onBindViewHolder(holder: BookMarkingBookVH, position: Int) {
        holder.bind(getItem(position))
    }
}


class BookMarkingBookVH(
    private val binding: ViewholderPagingBookItemBinding,
    val listener: BookMarkingBookClickListener
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: BookUiModel.Book) {
        with(binding) {
            title.text = item.title
            author.text = itemView.resources.getString(R.string.written_by, item.author)
            itemView.setOnClickListener { listener.onClick(item) }
        }
    }
}

interface BookMarkingBookClickListener {
    fun onClick(book: BookUiModel.Book)
}