package com.example.searchbook.book

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.searchbook.api.BookAPI
import com.example.searchbook.domain.BookUiModel
import retrofit2.HttpException
import java.io.IOException

internal class BookSearchPagingSource(
    private val bookAPI: BookAPI,
    private val query: String
) : PagingSource<Int, BookUiModel>() {
    companion object {
        private const val START_ITEM_INDEX = 1
        private const val MAX_ITEM_INDEX = 991
    }

    private var start = START_ITEM_INDEX

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookUiModel> {
        return try {
            if (query.isBlank()) {
                return LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }

            start = params.key ?: START_ITEM_INDEX

            val response = bookAPI.getBookList(query = query, start = start, sort = "date")
            val list: List<BookUiModel> = listOf<BookUiModel>()
            if (response.list.isEmpty()) {
                return LoadResult.Page(
                    data = list,
                    prevKey = null,
                    nextKey = null
                )
            }

            val bookList: List<BookUiModel> = response.list.map { BookUiModel.Book.of(it) }
            val newList = if (start == 1) list.plus(bookList) else bookList

            val nextKey =
                if ((start + response.list.size) < MAX_ITEM_INDEX) start + response.list.size else MAX_ITEM_INDEX

            return LoadResult.Page(
                data = newList,
                prevKey = null,
                nextKey = nextKey
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