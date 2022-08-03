package com.example.searchbook.book

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.searchbook.domain.BookUiModel
import com.example.searchbook.domain.GetFavoriteBookListUseCase
import kotlinx.coroutines.launch

internal class BookMarkingBookViewModel(
    val getFavoriteBookListUseCase: GetFavoriteBookListUseCase
) : ViewModel() {
    private val _favoriteList = MutableLiveData<BookUiModel.Book>()
    val favoriteList = _favoriteList.asFlow()

    fun fetch() {
        viewModelScope.launch {
            try {
                val lsit = getFavoriteBookListUseCase()
            } catch (e: Exception) {

            }
        }
    }
}