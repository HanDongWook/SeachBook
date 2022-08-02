package com.example.searchbook.book

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.searchbook.domain.BookDetail
import com.example.searchbook.domain.GetBookDetailUseCase
import kotlinx.coroutines.launch

internal class BookDetailViewModel(
    val isbn: Long,
    val getBookDetailUseCase: GetBookDetailUseCase
) : ViewModel() {
    private val _bookDetail = MutableLiveData<BookDetail>()
    val bookDetail = _bookDetail.asFlow()

    init {
        viewModelScope.launch {
            try {
                Log.d("test", "BookDetailViewModel : $isbn")
                val detail = getBookDetailUseCase(isbn.toString())
                _bookDetail.value = detail
            } catch (e: Exception) {
                Log.d("test", "e : $e")
            }
        }
    }
}