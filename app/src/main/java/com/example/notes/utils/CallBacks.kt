package com.example.notes.utils

import com.example.notes.models.NoteModel

interface CallBacks {
    interface NoteCallBack {
        fun onItemClick(position: Int, item: NoteModel)
        fun onEditClick(position: Int, item: NoteModel)
        fun onDeleteClick(position: Int, item: NoteModel)
    }
}