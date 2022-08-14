package com.example.notes.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notes.database.DatabaseHelper
import com.example.notes.ui.NoteViewModel

class ViewModelFactory(private val dbHelper: DatabaseHelper) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            return NoteViewModel(dbHelper) as T
        }

        throw IllegalArgumentException("Unknown class name")
    }
}