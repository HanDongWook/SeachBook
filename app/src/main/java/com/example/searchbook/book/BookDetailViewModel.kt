package com.example.searchbook.book

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import com.example.searchbook.domain.BookUiModel
import com.example.searchbook.domain.GetBookDetailUseCase

internal class BookDetailViewModel(
    val book: BookUiModel.Book,
    val getBookDetailUseCase: GetBookDetailUseCase
) : ViewModel() {
    private val _bookDetail = MutableLiveData<BookUiModel.Book>()
    val bookDetail = _bookDetail.asFlow()

    init {
        _bookDetail.value = book
//        viewModelScope.launch {
//            try {
//                Log.d("test", "BookDetailViewModel : $isbn")
//                val detail = getBookDetailUseCase(isbn.toString())
//                _bookDetail.value = detail
//            } catch (e: Exception) {
//                Log.d("test", "e : $e")
//            }
//        }
    }
}