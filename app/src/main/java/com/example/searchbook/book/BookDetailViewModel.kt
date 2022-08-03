package com.example.searchbook.book

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import com.example.searchbook.domain.BookUiModel
import com.example.searchbook.domain.GetBookDetailUseCase
import com.example.searchbook.utils.SingleLiveEvent

internal class BookDetailViewModel(
    val book: BookUiModel.Book,
    val getBookDetailUseCase: GetBookDetailUseCase
) : ViewModel() {
    sealed class Navigate {
        object Back : Navigate()
    }

    private val _bookDetail = MutableLiveData<BookUiModel.Book>()
    val bookDetail = _bookDetail.asFlow()

    val navigate = SingleLiveEvent<Navigate>()

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

    fun onClickFavorite() {

    }
}