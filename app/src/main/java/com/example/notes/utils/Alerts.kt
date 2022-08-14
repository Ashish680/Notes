package com.example.notes.utils

import android.content.Context
import android.widget.Toast

object Alerts {

    fun showToast(requireContext: Context, result: String?) {
        Toast.makeText(
            requireContext,
            result,
            Toast.LENGTH_LONG
        ).show()
    }
}