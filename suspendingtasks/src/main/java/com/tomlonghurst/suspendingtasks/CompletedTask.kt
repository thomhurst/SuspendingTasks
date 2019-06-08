package com.tomlonghurst.suspendingtasks

import com.google.android.gms.tasks.Task
import java.lang.Exception

class CompletedTask<T> internal constructor(task: Task<T>) {

    val result = task.result
    val exception = task.exception
    val isCancelled = task.isCanceled
    val isSuccessful = task.isSuccessful

    fun doIfSuccessful(action: (result: T) -> Unit) {
        if(isSuccessful) {
            action.invoke(result!!)
        }
    }

    fun doIfNotSuccessful(action: (exception: Exception) -> Unit) {
        if(!isSuccessful) {
            action.invoke(exception!!)
        }
    }

}