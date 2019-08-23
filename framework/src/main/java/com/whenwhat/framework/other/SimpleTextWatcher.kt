package com.whenwhat.framework.other

import android.text.Editable
import android.text.TextWatcher

interface SimpleTextWatcher : TextWatcher {

    override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) = Unit
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
    override fun afterTextChanged(editable: Editable) = Unit
}