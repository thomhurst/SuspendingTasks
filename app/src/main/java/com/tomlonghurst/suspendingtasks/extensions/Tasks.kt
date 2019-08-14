package com.tomlonghurst.suspendingtasks.extensions

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.tomlonghurst.suspendingtasks.models.CompletedTask
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun <T> Task<T>.await() = suspendCoroutine<CompletedTask<T?>> { continuation ->
    var resumed = false
    try {
        addOnCompleteListener { task ->
            if(!resumed) {
                resumed = true
                continuation.resume(CompletedTask(task))
            }
        }

        addOnCanceledListener {
            if(!resumed) {
                resumed = true
                continuation.resume(CompletedTask(task = null))
            }
        }

        Tasks.await(this)
    } catch (e: Exception) {
        if(!resumed) {
            resumed = true
            continuation.resume(CompletedTask(e))
        }
    }
}