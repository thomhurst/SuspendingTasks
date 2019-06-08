package com.tomlonghurst.suspendingtasks

import com.google.android.gms.tasks.Task
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun <T> Task<T>.awaitResult() = suspendCoroutine<CompletedTask<T?>> { continuation ->
    addOnCompleteListener { task ->
        continuation.resume(CompletedTask(task))
    }
}