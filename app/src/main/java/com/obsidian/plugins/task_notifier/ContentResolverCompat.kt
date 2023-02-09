package com.obsidian.plugins.task_notifier

import android.content.ContentResolver
import android.net.Uri
import java.lang.reflect.Method


object ContentResolverCompat {
    var available = false
    private var contentResolver: Class<*>? = null
    private var takePersistableUriPermission: Method? = null

    init {
        try {
            contentResolver = ContentResolver::class.java
            takePersistableUriPermission = (contentResolver as Class<ContentResolver>).getMethod(
                "takePersistableUriPermission", *arrayOf<Class<*>?>(
                    Uri::class.java, Int::class.javaPrimitiveType
                )
            )
            available = true
        } catch (e: Exception) {
            available = false
        }
    }

    fun takePersistableUriPermission(resolver: ContentResolver?, uri: Uri, modeFlags: Int) {
        if (available) {
            try {
                takePersistableUriPermission?.invoke(resolver, arrayOf<Any>(uri, modeFlags))
            } catch (e: Exception) {
                // Fail silently
            }
        }
    }
}