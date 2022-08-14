package com.example.notes.database

import com.example.notes.models.NoteModel

class DatabaseHelperImpl(private val appDatabase: AppDatabase) : DatabaseHelper {
    override suspend fun getActiveNoteItems(b: Boolean): List<NoteModel> =
        appDatabase.noteDao().getActiveNoteItems(b)


    override suspend fun getActiveNoteItemsASCOrder(b: Boolean): List<NoteModel> =
        appDatabase.noteDao().getActiveNoteItemsASCOrder(b)

    override suspend fun insertNoteItem(users: NoteModel): Long =
        appDatabase.noteDao().insertNoteWithTimestamp(users)

    override suspend fun updateNoteItem(users: NoteModel): Boolean =
        appDatabase.noteDao().updateNoteWithTimestamp(users)

    override suspend fun updateNoteItemWithoutModifyDate(model: NoteModel) =
        appDatabase.noteDao().updateNote(model)

    override suspend fun singleNoteById(updateId: Long): NoteModel? =
        appDatabase.noteDao().singleNoteById(updateId)

    override suspend fun deleteNoteItem(model: NoteModel) {
        appDatabase.noteDao().deleteNoteItem(model)
    }
}