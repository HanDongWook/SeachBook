package com.example.searchbook.domain

import com.example.searchbook.repository.BookRepository

internal class GetFavoriteBookListUseCase(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(): List<BookUiModel.Book> {
        return bookRepository.getFavoriteList()
    }
}