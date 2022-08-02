package com.example.searchbook.book

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.searchbook.api.BookAPI
import com.example.searchbook.api.BookResponse
import com.example.searchbook.domain.BookUiModel
import retrofit2.HttpException
import java.io.IOException

internal class BookSearchPagingSource(
    private val bookAPI: BookAPI,
    private val query: String
) : PagingSource<Int, BookUiModel>() {
    companion object {
        const val STARTING_PAGE_INDEX = 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookUiModel> {
        return try {
            if (query.isBlank()) {
                return LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }

            val position = params.key ?: STARTING_PAGE_INDEX

            val response = bookAPI.getBookList(query)
            val result = response.list.map { BookResponse.of(it) }
            Log.d("test", "result : $result")
            return LoadResult.Page(
                data = result,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = null
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, BookUiModel>): Int? {
//        return state.anchorPosition?.let { state.closestItemToPosition(it)?.isbn }
        return null
    }
}