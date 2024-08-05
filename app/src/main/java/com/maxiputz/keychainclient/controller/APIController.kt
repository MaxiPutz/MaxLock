package com.maxiputz.keychainclient.controller

import android.util.Log
import com.maxiputz.keychainclient.structs.ResponsData

import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun fetchDataFromServer(serverAddress: String): ResponsData = suspendCoroutine { continuation ->
    val url = "$serverAddress/api/get"
    val client = OkHttpClient()

    val request = Request.Builder()
        .url(url)
        .build()

    Log.d("url", serverAddress)
    Log.d("url", url)

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            continuation.resumeWithException(e)
        }

        override fun onResponse(call: Call, response: Response) {
            if (!response.isSuccessful) {
                continuation.resumeWithException(IOException("Failed to fetch data"))
            } else {
                val responseDataString = response.body?.string() ?: ""
                val gson = Gson()
                val resolveVal = gson.fromJson<ResponsData>(responseDataString, ResponsData::class.java)

                continuation.resume(resolveVal)
            }
        }
    })
}