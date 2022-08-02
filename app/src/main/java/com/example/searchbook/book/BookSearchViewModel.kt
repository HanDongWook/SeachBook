package com.example.searchbook.book

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.searchbook.domain.GetBookPagingListUseCase
import com.example.searchbook.utils.SingleLiveEvent

internal class BookSearchViewModel(
    val getBookPagingListUseCase: GetBookPagingListUseCase,
) : ViewModel() {
    sealed class Navigate {
        data class BookDetail(val isbn: Long) : Navigate()
    }

    private val searchBookQuery = MutableLiveData<String>()

    val navigate = SingleLiveEvent<Navigate>()

    val pagingList = searchBookQuery.switchMap { queryString ->
        getBookPagingListUseCase(queryString).cachedIn(viewModelScope)
    }

    init {
        searchBook("")
    }

    fun searchBook(query: String) {
        Log.d("test", "BookSearchViewModel edit text searchBook : $query ")
        searchBookQuery.postValue(query)
    }

    fun onBookClick(isbn: Long) {
        navigate.value = Navigate.BookDetail(isbn)
    }
}