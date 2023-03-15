package com.example.smartlab.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.example.smartlab.R

class GenericTextWatcher(
    private val currentView: EditText,
    private val nextView: EditText?,
    private val onLastEditTextFilled: () -> Unit = {}
) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        val text = s.toString()
        when (currentView.id) {
            R.id.etCode1 -> if (text.length == 1) nextView!!.requestFocus()
            R.id.etCode2 -> if (text.length == 1) nextView!!.requestFocus()
            R.id.etCode3 -> if (text.length == 1) nextView!!.requestFocus()
            R.id.etCode4 -> if (text.length == 1) onLastEditTextFilled()
        }
    }
}