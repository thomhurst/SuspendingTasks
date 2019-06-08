package com.tomlonghurst.suspendingtasks

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

class CompletedTask<T> internal constructor(task: Task<T>) {

    val result = task.result
    val exception = task.exception
    val isCancelled = task.isCanceled
    val isSuccessful = task.isSuccessful

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