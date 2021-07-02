package me.darthwithap.quixotegame

import android.view.MotionEvent
import android.view.View

abstract class TouchListener : View.OnTouchListener {
    private var lastClickTime = 0L

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val clickTime = System.currentTimeMillis()
                if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                    onDoubleTouch()
                    lastClickTime = 0
                } else onSingleTouch()
                lastClickTime = clickTime
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                onRelease()
            }
        }
        return false
    }

    abstract fun onSingleTouch()
    abstract fun onDoubleTouch()
    abstract fun onRelease()

    companion object {
        private const val DOUBLE_CLICK_TIME_DELTA = 5000
    }
}