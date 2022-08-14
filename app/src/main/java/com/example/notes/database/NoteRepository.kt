package com.example.notes.database

import com.example.notes.models.NoteModel

class NoteRepository(private val dbHelper: DatabaseHelper) {

    suspend fun getActiveNoteItems(b: Boolean): List<NoteModel>? {
        return dbHelper.getActiveNoteItems(b)
    }

    suspend fun insertNoteItem(value: NoteModel): Long {
        return dbHelper.insertNoteItem(value)
    }

    suspend fun updateNoteItem(value: NoteModel): NoteModel {
        dbHelper.updateNoteItem(value)
        return value
    }

    suspend fun updateNoteItemWithoutModifyDate(value: NoteModel): NoteModel {
        dbHelper.updateNoteItemWithoutModifyDate(value)
        return value
    }

    suspend fun singleNoteById(updateId: Long): NoteModel? {
        return dbHelper.singleNoteById(updateId)
    }

    suspend fun deleteNoteItem(item: NoteModel) {
        dbHelper.deleteNoteItem(item)
    }

    suspend fun getActiveNoteItemsASCOrder(b: Boolean): List<NoteModel> {
        return dbHelper.getActiveNoteItemsASCOrder(b)
    }
}