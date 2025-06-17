package com.tpjm.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tpjm.model.Word

@Dao
interface WordDao {

    @Insert
    suspend fun insert(word: Word)

    @Query("SELECT * FROM words WHERE word LIKE :query COLLATE NOCASE")
    suspend fun searchWords(query: String): List<Word>

    @Query("SELECT * FROM words")
    suspend fun getAllWords(): List<Word>

    @Query("SELECT * FROM words WHERE level = :level AND lesson = :lesson")
    suspend fun getWordsForLesson(level: String, lesson: Int): List<Word>
}