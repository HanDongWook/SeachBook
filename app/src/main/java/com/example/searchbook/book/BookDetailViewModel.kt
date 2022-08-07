package com.example.searchbook.book

import android.util.Log
import androidx.lifecycle.*
import com.example.searchbook.domain.*
import com.example.searchbook.utils.SingleLiveEvent
import kotlinx.coroutines.launch

internal class BookDetailViewModel(
    private val book: BookUiModel.Book,
    private val getBookDetailUseCase: GetBookDetailUseCase,
    private val getBookByIsbnUseCase: GetBookByIsbnUseCase,
    private val insertFavoriteBookUseCase: InsertFavoriteBookUseCase,
    private val deleteFavoriteBookUseCase: DeleteFavoriteBookUseCase
) : ViewModel() {
    private val _bookDetail = MutableLiveData<BookUiModel.Book>()
    val bookDetail = _bookDetail.asFlow()

    private val _isMyFavorite = MutableLiveData<Boolean>()
    val isMyFavorite: LiveData<Boolean> = _isMyFavorite

    val snackBar = SingleLiveEvent<String>()

    init {
        viewModelScope.launch {
            _bookDetail.value = book

            val book = getBookByIsbnUseCase(book.isbn.toString())
            Log.d("test", "book : $book book != null : ${book != null}")
            _isMyFavorite.value = (book != null)
        }

        //Get Book Detail By Using API
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
        viewModelScope.launch {
            try {
                if (_isMyFavorite.value == false) {
                    insertFavoriteBookUseCase(book)
                    snackBar.value = "내 즐겨찾기에 저장되었습니다."
                    _isMyFavorite.value = true
                } else {
                    deleteFavoriteBookUseCase(book.isbn.toString())
                    snackBar.value = "즐겨찾기에서 제거되었습니다."
                    _isMyFavorite.value = false
                }
            } catch (e: Exception) {

            }
        }
    }
}