# SuspendingTasks
Suspending Tasks for removing callbacks in Android

This library requires Kotlin and Kotlin Coroutines.


[![](https://jitpack.io/v/thomhurst/SuspendingTasks.svg)](https://jitpack.io/#thomhurst/SuspendingTasks)

Tired of callbacks? Making code harder to write? A callback in a callback?

```kotlin
fun doStuff() {

        FirebaseFirestore.getInstance()
            .collection("collection")
            .whereEqualTo("foo", "bar")
            .limit(1)
            .get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                val documents = queryDocumentSnapshots.documents
                if (documents.isNotEmpty()) {
                    documents.first().reference.delete()
                        .addOnSuccessListener { querySnapshot ->
                            doSomeOtherStuff()
                        }
                        .addOnFailureListener { exception ->
                            showError()
                        }
                }
            }
            
    }
```

Suspending Tasks adds an extension function, that allows you to write synchronous-style code, by wrapping these Task callbacks into suspending coroutines.

```kotlin
    suspend fun doStuff() {

        val search = FirebaseFirestore.getInstance()
            .collection("collection")
            .whereEqualTo("foo", "bar")
            .limit(1)
            .get()
            .await()

        if (search.isSuccessful && search.result!!.documents.isNotEmpty()) {

            val innerCollection = search.result
                .documents.first().reference.delete()
                .await()

            if (innerCollection.isSuccessful) {
                doSomeOtherStuff()
            } else {
                showError()
            }

        }

    }
```

Or skip straight to the objects you need!

```kotlin
    suspend fun doStuff() {

        FirebaseFirestore.getInstance()
            .collection("collection")
            .whereEqualTo("foo", "bar")
            .limit(1)
            .get()
            .await()
            .result
            ?.documents
            ?.first()
            ?.reference
            ?.collection("blah")
            ?.document("document")
            ?.get()
            ?.await()
            ?.doIfSuccessful { doSomething() }

    }
```

Nested callback issues can be removed. Code can become clearer and more easily manageable.

## Install

Add Jitpack to your repositories in your `build.gradle` file

```groovy
allprojects {
    repositories {
      // ...
      maven { url 'https://jitpack.io' }
    }
}
```

Add the below to your dependencies, again in your gradle.build file

```groovy
implementation 'com.github.thomhurst:SuspendingTasks:{version}'
```

# Usage

On a `Task<T>` object, call `await()`
You will receive a `CompletedTask<T>` object.

The `await()` method is a `suspend fun` meaning it needs to be launched on a coroutine. This is because this will be a long running task, and may take a while to run, and so we don't want to freeze our UI while we run it.

If you enjoy, please buy me a coffee :)

<a href="https://www.buymeacoffee.com/tomhurst" target="_blank"><img src="https://www.buymeacoffee.com/assets/img/custom_images/orange_img.png" alt="Buy Me A Coffee" style="height: auto !important;width: auto !important;" ></a>
