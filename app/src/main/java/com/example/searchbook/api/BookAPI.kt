package com.example.searchbook.api

import retrofit2.http.GET
import retrofit2.http.Query

interface BookAPI {
    @GET("v1/search/book.json")
    suspend fun getBookList(
        @Query("query") query: String,
        @Query("start") start: Int
    ): BooksResponse
}