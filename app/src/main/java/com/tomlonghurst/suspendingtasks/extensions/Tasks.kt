@file:Suppress("UNCHECKED_CAST")

package com.tomlonghurst.suspendingtasks.extensions

import com.google.android.gms.tasks.Task
import com.tomlonghurst.suspendingtasks.models.CompletedTask
import kotlinx.coroutines.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun <T> Task<T>.await(): CompletedTask<T> {
    // fast path
    if (isComplete) {
        val e = exception
        return if (e == null) {
            if (isCanceled) {
                CompletedTask(null, CancellationException("Task $this was cancelled."), true)
            } else {
                CompletedTask(result as T, null, false)
            }
        } else {
            CompletedTask(null, e, false)
        }
    }

    return suspendCoroutine { cont ->
        addOnCompleteListener { completedTask ->
            val e = exception
            if (e == null) {
                if (isCanceled) {
                    cont.resume(CompletedTask(null, CancellationException("Task $this was cancelled."), true))
                } else {
                    cont.resume(CompletedTask(completedTask.result as T, null, false))
                }
            } else {
                cont.resume(CompletedTask(null, e, false))
            }
        }
    }
}