package com.example.searchbook.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity)

    @Query("SELECT * fROM book")
    fun getAll(): List<BookEntity>

    @Query("SELECT * fROM book WHERE isbn = :isbn")
    fun getByISBN(isbn: String): BookEntity?

    @Query("DELETE fROM book WHERE isbn = :isbn")
    fun deleteByISBN(isbn: String)
}