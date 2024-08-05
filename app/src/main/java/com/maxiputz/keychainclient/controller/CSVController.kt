package com.maxiputz.keychainclient.controller

import android.util.Log
import com.maxiputz.keychainclient.structs.PasswordEntry


fun textToObject(csv: String) : List<PasswordEntry> {

    val obj = csv.split("\n").
    filter { ele -> ele != "" }.
    map { ele -> ele.split(",") }
        .filter { ele -> ele.size > 5 }
        .toMutableList()
    val header = obj.shiftLeft()


    for (ele in obj) {
        Log.d("objData", ele.toString())
        Log.d("objData", ele[0])
        Log.d("objData", ele[1])
        Log.d("objData", ele[2])

    }

    val content = obj.map { ele -> PasswordEntry(ele[0], ele[1],ele[2],ele[3],ele[4],ele[5]) }

    return content
}


fun <T> MutableList<T>.shiftLeft() {
    if (size > 1) {
        val first = this[0]
        for (i in 1 until size) {
            this[i - 1] = this[i]
        }
        this[size - 1] = first
    }
}