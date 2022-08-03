package com.example.searchbook.domain

import com.example.searchbook.repository.BookRepository

internal class GetBookByIsbnUseCase(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(isbn: String): BookUiModel.Book? {
        return bookRepository.getBookByIsbn(isbn)
    }
}