package com.example.searchbook.api

import android.os.Parcelable
import com.example.searchbook.domain.BookUiModel
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

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

    @SerializedName(value = "price")
    val price: Int,

    @SerializedName(value = "publisher")
    val publisher: String,

    @SerializedName(value = "isbn")
    val isbn: Long,

    @SerializedName(value = "pubdate")
    val pubdate: String,

    @SerializedName(value = "description")
    val description: String
) : Parcelable {
    companion object {
        fun of(res: BookResponse): BookUiModel.Book {
            return BookUiModel.Book(
                res.isbn,
                res.title,
                res.author
            )
        }
    }
}