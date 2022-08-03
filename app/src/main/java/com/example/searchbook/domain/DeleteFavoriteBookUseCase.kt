package com.example.searchbook.domain

import com.example.searchbook.repository.BookRepository

internal class DeleteFavoriteBookUseCase(
    private val bookRepository: BookRepository
) {
    operator suspend fun invoke(isbn: String) {
        return bookRepository.deleteFavoriteList(isbn)
    }
}