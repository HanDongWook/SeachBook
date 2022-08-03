package com.example.searchbook.book

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import com.example.searchbook.domain.BookUiModel

internal class BookMarkingBookViewModel(
) : ViewModel() {
    private val _favoriteList = MutableLiveData<BookUiModel.Book>()
    val favoriteList = _favoriteList.asFlow()

    fun fetch() {

    }
}