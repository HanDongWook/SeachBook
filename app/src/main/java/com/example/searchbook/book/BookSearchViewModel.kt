package com.example.searchbook.book

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.searchbook.domain.BookUiModel
import com.example.searchbook.domain.GetBookPagingListUseCase
import com.example.searchbook.utils.SingleLiveEvent

internal class BookSearchViewModel(
    private val getBookPagingListUseCase: GetBookPagingListUseCase,
) : ViewModel() {
    sealed class Navigate {
        data class BookDetail(val book: BookUiModel.Book) : Navigate()
        object MyFavorite : Navigate()
    }

    private val searchBookQuery = MutableLiveData<String>()

    val navigate = SingleLiveEvent<Navigate>()

    val pagingList = searchBookQuery.switchMap { queryString ->
        getBookPagingListUseCase(queryString).cachedIn(viewModelScope)
    }

    init {
        init()
    }

    fun searchBook(query: String) {
        if (searchBookQuery.value != query) {
            searchBookQuery.postValue(query)
        }
    }

    fun init() {
        searchBook("")
    }

    fun goMyFavorite() {
        navigate.value = Navigate.MyFavorite
    }

    fun onBookClick(book: BookUiModel.Book) {
        navigate.value = Navigate.BookDetail(book)
    }
}