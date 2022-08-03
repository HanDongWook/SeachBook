package com.example.searchbook.di

import com.example.searchbook.api.ApiProvider
import com.example.searchbook.book.BookDetailViewModel
import com.example.searchbook.book.BookMarkingBookViewModel
import com.example.searchbook.book.BookSearchViewModel
import com.example.searchbook.domain.*
import com.example.searchbook.repository.BookRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { BookRepository(get()) }
    single { ApiProvider() }

    single { GetBookDetailUseCase(get()) }
    single { GetBookPagingListUseCase(get()) }
    single { GetFavoriteBookListUseCase(get()) }
    single { GetBookByIsbnUseCase(get()) }
    single { InsertFavoriteBookUseCase(get()) }
    single { DeleteFavoriteBookUseCase(get()) }

    viewModel { BookSearchViewModel(get()) }
    viewModel { BookMarkingBookViewModel(get()) }
    viewModel { (book: BookUiModel.Book) -> BookDetailViewModel(book, get(), get(), get(), get()) }
}
