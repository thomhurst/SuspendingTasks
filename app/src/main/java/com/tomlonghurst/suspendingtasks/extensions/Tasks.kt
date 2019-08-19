@file:Suppress("UNCHECKED_CAST")

package com.tomlonghurst.suspendingtasks.extensions

import com.google.android.gms.tasks.Task
import com.tomlonghurst.suspendingtasks.models.CompletedTask
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun <T> Task<T>.await(): CompletedTask<T> {
    if (isComplete) {
        return CompletedTask(this)
    }

    return suspendCoroutine { cont ->
        addOnCompleteListener { completedTask ->
            cont.resume(CompletedTask(completedTask))
        }
    }
}