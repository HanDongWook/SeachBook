package com.example.searchbook.api

import android.os.Parcelable
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
) : Parcelable