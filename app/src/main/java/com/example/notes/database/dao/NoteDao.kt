package com.example.notes.database.dao

import androidx.room.*
import com.example.notes.models.NoteModel

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertNote(data: NoteModel): Long

    fun insertNoteWithTimestamp(data: NoteModel): Long {
        val id = insertNote(data.apply {
            createdAt = System.currentTimeMillis()
            modifiedAt = System.currentTimeMillis()
        })
        return id
    }

    @Update
    fun updateNote(data: NoteModel)

    fun updateNoteWithTimestamp(data: NoteModel): Boolean {
        updateNote(data.apply {
            modifiedAt = System.currentTimeMillis()
        })
        return data.status
    }

    @Delete
    fun deleteNoteItem(data: NoteModel)


    @Query("SELECT * FROM NoteModel WHERE status = :status ORDER BY modified_at DESC")
    fun getActiveNoteItems(status: Boolean): List<NoteModel>

    @Query("SELECT * FROM NoteModel WHERE status = :status ORDER BY modified_at ASC")
    fun getActiveNoteItemsASCOrder(status: Boolean): List<NoteModel>

    @Query("SELECT * FROM NoteModel WHERE id = :updateId")
    fun singleNoteById(updateId: Long): NoteModel?
}