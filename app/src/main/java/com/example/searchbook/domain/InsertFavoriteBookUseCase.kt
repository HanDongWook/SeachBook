package com.example.searchbook.domain

import com.example.searchbook.repository.BookRepository

internal class InsertFavoriteBookUseCase(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(book: BookUiModel.Book) {
        return bookRepository.insertFavoriteBook(book)
    }
}