package com.example.searchbook.book

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.searchbook.domain.BookUiModel
import com.example.searchbook.domain.GetFavoriteBookListUseCase
import com.example.searchbook.utils.SingleLiveEvent
import kotlinx.coroutines.launch

internal class BookMarkingBookViewModel(
    val getFavoriteBookListUseCase: GetFavoriteBookListUseCase
) : ViewModel() {

    sealed class Navigate {
        data class BookDetail(val book: BookUiModel.Book) : Navigate()
    }

    private val _favoriteList = MutableLiveData<List<BookUiModel.Book>>()
    val favoriteList = _favoriteList.asFlow()

    val navigate = SingleLiveEvent<Navigate>()

    fun fetch() {
        viewModelScope.launch {
            try {
                val list = getFavoriteBookListUseCase()
                _favoriteList.value = list
            } catch (e: Exception) {

            }
        }
    }

    fun onBookClick(book: BookUiModel.Book) {
        navigate.value = Navigate.BookDetail(book)
    }
}