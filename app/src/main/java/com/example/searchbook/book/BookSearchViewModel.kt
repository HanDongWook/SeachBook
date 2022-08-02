package com.example.searchbook.book

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.cachedIn
import androidx.paging.insertHeaderItem
import com.example.searchbook.domain.BookUiModel
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
        Log.d("test", "BookSearchViewModel switchMap : $queryString ")
        getBookPagingListUseCase(queryString).cachedIn(viewModelScope).map {
            it.insertHeaderItem(item = BookUiModel.Header)
        }
    }

    fun searchBook(query: String) {
        Log.d("test", "BookSearchViewModel edit text searchBook : $query ")
        searchBookQuery.postValue(query)
    }

    fun onBookClick(isbn: Long) {
        navigate.value = Navigate.BookDetail(isbn)
    }
}