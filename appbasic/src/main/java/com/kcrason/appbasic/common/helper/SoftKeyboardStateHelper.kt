package com.kcrason.appbasic.common.helper

import android.graphics.Rect
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.kcrason.appbasic.listeners.OnSoftKeyboardStateListener

import java.util.LinkedList


class SoftKeyboardStateHelper @JvmOverloads constructor(
    private val parentView: ViewGroup,
    private var isSoftKeyboardOpened: Boolean = false
) : ViewTreeObserver.OnGlobalLayoutListener {

    private val listeners = LinkedList<OnSoftKeyboardStateListener>()

    init {
        parentView.rootView.viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    override fun onGlobalLayout() {
        val r = Rect()
        parentView.rootView.getWindowVisibleDisplayFrame(r)
        val heightDiff = parentView.rootView.height - r.bottom
        if (!isSoftKeyboardOpened && heightDiff > 200) {
            isSoftKeyboardOpened = true
            notifyOnSoftKeyboardOpened(heightDiff)
        } else if (isSoftKeyboardOpened && heightDiff < 200) {
            isSoftKeyboardOpened = false
            notifyOnSoftKeyboardClosed()
        }
    }


    fun addSoftKeyboardStateListener(listener: OnSoftKeyboardStateListener) {
        listeners.add(listener)
    }


    private fun notifyOnSoftKeyboardOpened(keyboardHeightInPx: Int) {
        for (listener in listeners) {
            listener.onSoftKeyboardOpened(keyboardHeightInPx)
        }
    }

    private fun notifyOnSoftKeyboardClosed() {
        for (listener in listeners) {
            listener.onSoftKeyboardClosed()
        }
    }
}
