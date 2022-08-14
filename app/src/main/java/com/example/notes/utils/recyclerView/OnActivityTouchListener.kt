package com.example.notes.utils.recyclerView

import android.view.MotionEvent

interface OnActivityTouchListener {
    fun getTouchCoordinates(ev: MotionEvent?)
}