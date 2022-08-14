package com.example.notes.database

import com.example.notes.models.NoteModel

interface DatabaseHelper {
    suspend fun getActiveNoteItems(b: Boolean): List<NoteModel>

    suspend fun getActiveNoteItemsASCOrder(b: Boolean): List<NoteModel>

    suspend fun insertNoteItem(users: NoteModel): Long

    suspend fun updateNoteItem(users: NoteModel): Boolean

    suspend fun updateNoteItemWithoutModifyDate(model: NoteModel)

    suspend fun singleNoteById(updateId: Long): NoteModel?

    suspend fun deleteNoteItem(model: NoteModel)
}