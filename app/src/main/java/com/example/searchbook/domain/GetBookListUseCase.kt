package com.example.searchbook.domain

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.searchbook.repository.BookRepository

internal class GetBookPagingListUseCase(
    private val bookRepository: BookRepository
) {
    operator fun invoke(query: String): LiveData<PagingData<BookUiModel>> {
        return bookRepository.getBookPagingList(query)
    }
}