package com.example.notes.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.constants.ActionType
import com.example.notes.database.DatabaseHelper
import com.example.notes.database.NoteRepository
import com.example.notes.models.NoteModel
import com.example.notes.utils.Resource
import kotlinx.coroutines.launch

class NoteViewModel(private val dbHelper: DatabaseHelper) : ViewModel() {
    private val mainRepository = NoteRepository(dbHelper)

    private val item = MutableLiveData<Resource<NoteModel?>?>()

    fun getItem(): MutableLiveData<Resource<NoteModel?>?> {
        return item
    }

    private val itemListActive = MutableLiveData<Resource<List<NoteModel?>?>>()

    fun getActiveItemList(): MutableLiveData<Resource<List<NoteModel?>?>> {
        return itemListActive
    }

    fun fetchActiveNoteItems() {
        viewModelScope.launch {
            itemListActive.postValue(Resource.loading(null, ActionType.VIEW))
            try {
                val usersFromDb = mainRepository.getActiveNoteItems(true)
                itemListActive.postValue(Resource.success(usersFromDb, ActionType.VIEW))
            } catch (e: Exception) {
                itemListActive.postValue(
                    Resource.error(
                        e.message.toString(),
                        null,
                        ActionType.VIEW
                    )
                )
            }
        }
    }

    fun insertNote(value: NoteModel) {
        viewModelScope.launch {
            item.postValue(Resource.loading(null, ActionType.ADD))
            val usersFromDb = mainRepository.insertNoteItem(value)
            value.id = usersFromDb
            item.postValue(Resource.success(value, ActionType.ADD))
            Log.e("NoteViewModel ", "usersFromDb $usersFromDb $value")
        }
    }

    fun updateNoteItem(position: Int, itemRem: NoteModel, action: ActionType) {
        viewModelScope.launch {
            if (action == ActionType.DELETE)
                itemRem.status = false
            val usersFromDb = mainRepository.updateNoteItem(itemRem)
            if (action == ActionType.EDIT)
                item.postValue(Resource.success(usersFromDb, action))
            Log.e("NoteViewModel ", "$action+ $usersFromDb")
        }
    }

    fun singleNoteById(updateId: Long) {
        viewModelScope.launch {
            itemListActive.postValue(Resource.loading(null, ActionType.VIEW))
            try {
                val usersFromDb = mainRepository.singleNoteById(updateId)
                item.postValue(Resource.success(usersFromDb, ActionType.VIEW))
                Log.e("singleNoteById ", " $usersFromDb")
            } catch (e: Exception) {
                item.postValue(Resource.error(e.message.toString(), null, ActionType.VIEW))
            }
        }
    }

    fun deleteNoteItem(item: NoteModel) {
        viewModelScope.launch {
            mainRepository.deleteNoteItem(item)
        }
    }
}