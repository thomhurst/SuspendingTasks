@file:Suppress("UNCHECKED_CAST")

package com.tomlonghurst.suspendingtasks.extensions

import com.google.android.gms.tasks.Task
import com.tomlonghurst.suspendingtasks.models.CompletedTask
import kotlin.coroutines.suspendCoroutine

suspend fun <T> Task<T>.await(): CompletedTask<T> {
    if (isComplete) {
        return CompletedTask(result, exception, isCanceled)
    }

    return suspendCoroutine { cont ->
        addOnCompleteListener { completedTask ->
            CompletedTask(completedTask.result, completedTask.exception, completedTask.isCanceled)
        }
    }
}