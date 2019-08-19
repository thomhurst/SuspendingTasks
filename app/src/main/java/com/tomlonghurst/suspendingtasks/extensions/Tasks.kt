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
                CompletedTask(CancellationException("Task $this was cancelled."))
            } else {
                CompletedTask(this)
            }
        } else {
            CompletedTask(e)
        }
    }

    return suspendCoroutine { cont ->
        addOnCompleteListener { completedTask ->
            val e = exception
            if (e == null) {
                if (isCanceled) {
                    cont.resume(CompletedTask(CancellationException("Task $this was cancelled.")))
                } else {
                    cont.resume(CompletedTask(completedTask))
                }
            } else {
                cont.resume(CompletedTask(e))
            }
        }
    }
}