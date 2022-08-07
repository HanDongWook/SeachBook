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
    private var yLatest = ""

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookUiModel> {
        return try {
            val searchBar: List<BookUiModel> = listOf<BookUiModel>().addSearchBar(query)
            if (query.isBlank()) {
                return LoadResult.Page(
                    data = searchBar,
                    prevKey = null,
                    nextKey = null
                )
            }

            start = params.key ?: START_ITEM_INDEX

            val response = bookAPI.getBookList(query = query, start = start, sort = "date")

            if (response.list.isEmpty()) {
                return LoadResult.Page(
                    data = searchBar,
                    prevKey = null,
                    nextKey = null
                )
            }

            val bookList: List<BookUiModel> =
                response.list.map { BookUiModel.Book.of(it) }.addDateSeparator()
            val newList = if (start == 1) searchBar.plus(bookList) else bookList

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

    private fun List<BookUiModel>.addSearchBar(query: String): List<BookUiModel> {
        return this.plus(BookUiModel.SearchBar(query))
    }

    private fun List<BookUiModel>.addDateSeparator(): List<BookUiModel> {
        val newList = mutableListOf<BookUiModel>()
        this.forEach {
            val yCurrent = (it as BookUiModel.Book).pubdate.substring(0 until 4)
            if (yLatest != yCurrent) {
                newList.add(BookUiModel.DateSeparator(yCurrent))
                yLatest = yCurrent
            }
            newList.add(it)
        }
        return newList
    }
}