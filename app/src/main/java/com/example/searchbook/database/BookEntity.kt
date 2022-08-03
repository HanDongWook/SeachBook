package com.example.searchbook.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.searchbook.domain.BookUiModel

@Entity(tableName = "book")
data class BookEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val isbn: Long,
    val title: String,
    val author: String,
    val pubdate: String,
    val image: String?,
    val discount: String?,
    val publisher: String?,
    val description: String?
) {
    companion object {
        fun of(res: BookUiModel.Book): BookEntity {
            return BookEntity(
                id = 0,
                isbn = res.isbn,
                title = res.title,
                author = res.author,
                image = res.image,
                discount = res.discount,
                publisher = res.publisher,
                pubdate = res.pubdate,
                description = res.description
            )
        }
    }
}