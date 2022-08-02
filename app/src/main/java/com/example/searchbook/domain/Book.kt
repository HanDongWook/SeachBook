package com.example.searchbook.domain

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

sealed class BookUiModel {
    @Keep
    @Parcelize
    data class Book(
        val isbn: Long,
        val title: String,
        val author: String
    ) : Parcelable, BookUiModel() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Book

            if (isbn != other.isbn) return false
            if (title != other.title) return false
            if (author != other.author) return false

            return true
        }
    }

    data class Header(
        val query: String
    ) : BookUiModel()
}

@Keep
@Parcelize
data class BookDetail(
    val isbn: String,
    val image: String?,
    val title: String?,
    val author: String?,
    val price: String?,
    val discount: String?,
    val publisher: String?,
    val pubdate: String?,
    val description: String?
) : Parcelable
