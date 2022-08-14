package com.example.notes.utils

import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    @SuppressLint("SimpleDateFormat")
    fun setDateFormat(modifiedAt: Long): CharSequence? {
        val d = Date(modifiedAt)
        val f: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        return f.format(d)
    }
}