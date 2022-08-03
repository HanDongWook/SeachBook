package com.example.searchbook.domain

import android.os.Parcelable
import com.example.searchbook.api.BookResponse
import com.example.searchbook.database.BookEntity
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
        val pubdate: String,
        val description: String?
    ) : Parcelable, BookUiModel() {

        companion object {
            fun of(entity: BookEntity): Book {
                return Book(
                    isbn = entity.isbn,
                    title = entity.title,
                    author = entity.author,
                    image = entity.image,
                    discount = entity.discount,
                    publisher = entity.publisher,
                    pubdate = entity.pubdate,
                    description = entity.description
                )
            }

            fun of(res: BookResponse): Book {
                return Book(
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

        override fun hashCode(): Int {
            var result = isbn.hashCode()
            result = 31 * result + title.hashCode()
            result = 31 * result + author.hashCode()
            result = 31 * result + (image?.hashCode() ?: 0)
            result = 31 * result + (discount?.hashCode() ?: 0)
            result = 31 * result + (publisher?.hashCode() ?: 0)
            result = 31 * result + (pubdate.hashCode())
            result = 31 * result + (description?.hashCode() ?: 0)
            return result
        }
    }

    data class SearchBar(
        val query: String
    ) : BookUiModel()

    data class DateSeparator(
        val date: String
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
