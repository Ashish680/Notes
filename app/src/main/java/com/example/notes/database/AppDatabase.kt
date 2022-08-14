package com.example.notes.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.notes.database.dao.NoteDao
import com.example.notes.models.NoteModel

@Database(
    entities = [NoteModel::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}