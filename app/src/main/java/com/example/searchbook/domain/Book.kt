package com.example.searchbook.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class BookUiModel {

    @Parcelize
    data class Book(
        val isbn: Long,
        val title: String,
        val author: String,
        val image: String?,
        val discount: String?,
        val publisher: String?,
        val pubdate: String?,
        val description: String?
    ) : Parcelable, BookUiModel() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Book

            if (isbn != other.isbn) return false
            if (title != other.title) return false
            if (author != other.author) return false
            if (image != other.image) return false
            if (discount != other.discount) return false
            if (publisher != other.publisher) return false
            if (pubdate != other.pubdate) return false
            if (description != other.description) return false

            return true
        }
    }

    data class Header(
        val query: String
    ) : BookUiModel()
}

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
