package com.tomlonghurst.suspendingtasks.models

import com.google.android.gms.tasks.Task

class CompletedTask<T> internal constructor(task: Task<T>) {

    val isSuccessful = task.isSuccessful
    val isCancelled = task.isCanceled
    val result = task.result
    val exception = task.exception

    fun doIfSuccessful(action: (result: T) -> Unit): CompletedTask<T> {
        if(isSuccessful) {
            action.invoke(result!!)
        }
        return this
    }

    fun doIfError(action: (exception: Exception) -> Unit): CompletedTask<T> {
        if(!isSuccessful) {
            action.invoke(exception!!)
        }
        return this
    }

}