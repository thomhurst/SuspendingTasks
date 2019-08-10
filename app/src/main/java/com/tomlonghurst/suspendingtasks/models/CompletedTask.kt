package com.tomlonghurst.suspendingtasks.models

import com.google.android.gms.tasks.Task
import java.lang.Exception

class CompletedTask<T> internal constructor(private val task: Task<T>?) {

    internal constructor(e: Exception) : this(null) {
        exception = e
    }

    val result = task?.result

    var exception: Exception? = null
        get() {
            if (field != null) {
                return field
            }
            return task?.exception
        }

    val isCancelled = task?.isCanceled ?: true
    val isSuccessful = task?.isSuccessful ?: false

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