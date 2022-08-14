package com.example.notes.utils

import com.example.notes.constants.ActionType
import com.example.notes.constants.Status

data class Resource<out T>(
    val status: Status,
    val data: T?,
    val message: String?,
    val action: ActionType
) {

    companion object {

        fun <T> success(data: T?, action: ActionType): Resource<T> {
            return Resource(Status.SUCCESS, data, null, action)
        }

        fun <T> error(msg: String, data: T?, action: ActionType): Resource<T> {
            return Resource(Status.ERROR, data, msg, action)
        }

        fun <T> loading(data: T?, action: ActionType): Resource<T> {
            return Resource(Status.LOADING, data, null, action)
        }

    }

}