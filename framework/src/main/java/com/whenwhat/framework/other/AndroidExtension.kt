package com.whenwhat.framework.other

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.util.TypedValue
import android.widget.EditText
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.whenwhat.framework.MVVMApplication

val Number.dp: Int
    get() = MVVMApplication.context.dpToPx(toFloat())

fun Context.dpToPx(dp: Float): Int = (dp * resources.displayMetrics.density).toInt()

fun Context.spToPx(sp: Float): Float =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.displayMetrics)

val Bundle.objects: List<Any>
    get() = keySet().mapNotNull { get(it) }

@Suppress("UNCHECKED_CAST")
inline fun <reified T> Bundle.getObject(): T? {
    val clazz = T::class.java
    return objects.find { clazz.isInstance(it) } as? T
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Any> Bundle.allObjects(): List<T> {
    val clazz = T::class.java
    return objects.mapNotNull { param ->
        param.takeIf { clazz.isInstance(it) } as? T
    }
}

inline fun EditText.onTextChange(crossinline action: (String) -> Unit) {
    addTextChangedListener(object : SimpleTextWatcher {
        override fun afterTextChanged(editable: Editable) {
            action(editable.toString())
        }
    })
}

fun RecyclerView.addOnEndItemsListener(threshold: Int = 1, onEnd: () -> Unit) {
    val lastVisiblePositionProxy: () -> Int = when (val lm = layoutManager) {
        is LinearLayoutManager -> ({ lm.findLastVisibleItemPosition() })
        is GridLayoutManager -> ({ lm.findLastVisibleItemPosition() })
        else -> throw IllegalArgumentException("Unsupported operation for layoutManager ($lm)")
    }
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val total = adapter?.itemCount ?: return
            if (total <= lastVisiblePositionProxy() + threshold)
                onEnd()
        }
    })
}