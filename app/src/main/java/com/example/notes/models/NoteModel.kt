package com.example.notes.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.notes.constants.NoteType

@Entity
data class NoteModel(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "url") var url: String? = null,
    @ColumnInfo(name = "note_type") val noteType: NoteType,
    @ColumnInfo(name = "status") var status: Boolean = true,
    @ColumnInfo(name = "created_at") var createdAt: Long = 0,
    @ColumnInfo(name = "modified_at") var modifiedAt: Long = 0
)
