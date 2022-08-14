package com.example.notes.ui

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.notes.utils.Alerts

open class BaseFragment : Fragment() {
    private var activity: BaseActivity? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) activity = context
    }

    fun showToastMethod(msg: String?) {
        activity?.let { Alerts.showToast(it, msg ?: "") }
    }
}