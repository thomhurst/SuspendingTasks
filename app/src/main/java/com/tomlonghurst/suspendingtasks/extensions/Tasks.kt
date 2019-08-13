package com.tomlonghurst.suspendingtasks.extensions

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.tomlonghurst.suspendingtasks.models.CompletedTask
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun <T> Task<T>.await() = suspendCoroutine<CompletedTask<T?>> { continuation ->
    try {
        addOnCompleteListener { task ->
            continuation.resume(CompletedTask(task))
        }

        addOnCanceledListener {
            continuation.resume(CompletedTask(task = null))
        }

        Tasks.await(this)
    } catch (e: Exception) {
        continuation.resume(CompletedTask(e))
    }
}