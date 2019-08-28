package com.whenwhat.framework.property

import android.os.Bundle
import com.whenwhat.framework.storage.PropertyStorage
import com.whenwhat.framework.storage.get
import com.whenwhat.framework.storage.set
import java.io.Serializable

@Suppress("UNCHECKED_CAST")
fun <T : Serializable> bundleProperty(defaultValue: T, bundle: Bundle, onChange: ((T) -> Unit)? = null): PropertyDelegate<T> {
    var value: T? = null
    return PropertyDelegate({ property, newValue ->
        value = newValue
        bundle.putSerializable("${property.name}_property", newValue)
    }, { property ->
        value ?: bundle.getSerializable("${property.name}_property") as? T ?: defaultValue
    }).apply {
        if (onChange != null)
            addOnValueChangeListener(onChange)
    }
}

fun <T> property(initialValue: T, onChange: ((T) -> Unit)? = null): PropertyDelegate<T> {
    var value: T = initialValue
    return PropertyDelegate({ _, newValue ->
        value = newValue
    }, { value }).apply {
        if (onChange != null)
            addOnValueChangeListener(onChange)
    }
}

inline fun <reified T> sharedPreferenceProperty(name: String, defaultValue: T, noinline onChange: ((T) -> Unit)? = null): PropertyDelegate<T> {
    return PropertyDelegate({ _, newValue ->
        PropertyStorage[name] = newValue
    }, {
        PropertyStorage[name] ?: defaultValue
    }).apply {
        if (onChange != null)
            addOnValueChangeListener(onChange)
    }
}