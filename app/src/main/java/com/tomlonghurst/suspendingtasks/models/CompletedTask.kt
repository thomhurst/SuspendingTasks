package com.tomlonghurst.suspendingtasks.models

class CompletedTask<T> internal constructor(val result: T?, val exception: Exception?, val isCancelled: Boolean) {

    val isSuccessful = exception != null

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