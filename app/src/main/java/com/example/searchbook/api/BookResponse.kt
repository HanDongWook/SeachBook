package com.example.searchbook.api

import android.os.Parcelable
import com.example.searchbook.domain.BookUiModel
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class BooksResponse(
    @SerializedName(value = "items")
    val list: List<BookResponse>,
)

@Parcelize
data class BookResponse(
    @SerializedName(value = "title")
    val title: String,

    @SerializedName(value = "author")
    val author: String,

    @SerializedName(value = "discount")
    val discount: Int,

    @SerializedName(value = "publisher")
    val publisher: String,

    @SerializedName(value = "isbn")
    val isbn: Long,

    @SerializedName(value = "image")
    val image: String,

    @SerializedName(value = "pubdate")
    val pubdate: String,

    @SerializedName(value = "description")
    val description: String
) : Parcelable {
    companion object {
        fun of(res: BookResponse): BookUiModel.Book {
            return BookUiModel.Book(
                isbn = res.isbn,
                title = res.title,
                author = res.author,
                image = res.image,
                discount = res.discount.toString(),
                publisher = res.publisher,
                pubdate = res.pubdate,
                description = res.description
            )
        }
    }
}