package com.example.searchbook.repository

import androidx.lifecycle.map
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.insertHeaderItem
import androidx.paging.insertSeparators
import androidx.paging.liveData
import com.example.searchbook.Application
import com.example.searchbook.api.ApiProvider
import com.example.searchbook.api.BookAPI
import com.example.searchbook.book.BookSearchPagingSource
import com.example.searchbook.database.AppDatabase
import com.example.searchbook.database.BookEntity
import com.example.searchbook.domain.BookDetail
import com.example.searchbook.domain.BookUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import javax.xml.parsers.DocumentBuilderFactory


internal class BookRepository(
    private val apiProvider: ApiProvider
) {
    private val bookApi by lazy { apiProvider.create(BookAPI::class) }
    private val appDb by lazy { AppDatabase.getDatabase(Application.context) }

    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }

    suspend fun insertFavoriteBook(book: BookUiModel.Book) = withContext(Dispatchers.IO) {
        appDb.bookDao().insertBook(BookEntity.of(book))
    }

    suspend fun deleteFavoriteList(isbn: String) = withContext(Dispatchers.IO) {
        appDb.bookDao().deleteByISBN(isbn)
    }

    suspend fun getBookByIsbn(isbn: String): BookUiModel.Book? = withContext(Dispatchers.IO) {
        val book = appDb.bookDao().getByISBN(isbn)
        if (book == null) {
            return@withContext null
        } else {
            return@withContext BookUiModel.Book.of(book)
        }
    }

    suspend fun getFavoriteList(): List<BookUiModel.Book> = withContext(Dispatchers.IO) {
        val bookList = appDb.bookDao().getAll()
        return@withContext bookList.map {
            BookUiModel.Book.of(it)
        }
    }

    fun getBookPagingList(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE
            )
        ) { BookSearchPagingSource(bookApi, query) }.liveData
            .map {
                it.insertHeaderItem(item = BookUiModel.SearchBar(query))
            }.map {
                it.insertSeparators { before: BookUiModel?, after: BookUiModel? ->
                    if (before is BookUiModel.SearchBar && after != null) {
                        val year = (after as BookUiModel.Book).pubdate.substring(0 until 4)
                        BookUiModel.DateSeparator(year)
                    } else if (before is BookUiModel.Book && after != null) {
                        val beforeYear = before.pubdate.substring(0 until 4)
                        val afterYear = (after as BookUiModel.Book).pubdate.substring(0 until 4)
                        if (beforeYear != afterYear) {
                            BookUiModel.DateSeparator(afterYear)
                        } else null
                    } else null
                }
            }

    suspend fun getBookDetail(isbn: String): BookDetail = withContext(Dispatchers.IO) {
        val d_isbn = URLEncoder.encode(isbn, "UTF-8")
        val apiURL = "https://openapi.naver.com/v1/search/book_adv.xml?d_isbn=$d_isbn"
        val url = URL(apiURL)
        val huc: HttpURLConnection = url.openConnection() as HttpURLConnection
        huc.requestMethod = "GET"
        huc.setRequestProperty("X-Naver-Client-Id", "Yx4EzmZSd6ZfY2LJaY05")
        huc.setRequestProperty("X-Naver-Client-Secret", "uiWY05fyTt")
        val responseCode = huc.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val dbFactory = DocumentBuilderFactory.newInstance()
            val dBuilder = dbFactory.newDocumentBuilder()
            val doc = dBuilder.parse(huc.inputStream)

            val element = doc.documentElement
            element.normalize()

            val nList = doc.getElementsByTagName("item")
            for (i in 0..nList.length) {
                val node = nList.item(i)
                if (node.nodeType == Node.ELEMENT_NODE) {
                    val element2 = node as Element

                    val title = getValue("title", element2)
                    val image = getValue("image", element2)
                    val isbn = getValue("isbn", element2)
                    val author = getValue("author", element2)
                    val price = getValue("price", element2)
                    val discount = getValue("discount", element2)
                    val publisher = getValue("publisher", element2)
                    val pubdate = getValue("pubdate", element2)
                    val description = getValue("description", element2)

                    return@withContext BookDetail(
                        isbn = isbn!!,
                        image = image,
                        title = title,
                        author = author,
                        price = price,
                        discount = discount,
                        publisher = publisher,
                        pubdate = pubdate,
                        description = description
                    )

                }
            }
            throw RuntimeException("XML Parsing을 실패했습니다.")
        } else {
            throw RuntimeException("API 응답을 읽는데 실패했습니다.")
        }
    }

    private fun getValue(tag: String, element: Element): String? {
        val nodeList = element.getElementsByTagName(tag).item(0)
        if (nodeList == null) {
            return null
        } else {
            val childNode = nodeList.childNodes
            val node = childNode.item(0)
            return node.nodeValue
        }
    }
}