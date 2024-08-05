package com.maxiputz.keychainclient.store

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.maxiputz.keychainclient.controller.PreferencController
import com.maxiputz.keychainclient.structs.PasswordEntry
import com.maxiputz.keychainclient.structs.ResponsData
import kotlinx.coroutines.launch

class Store(application: Application) : AndroidViewModel(application) {
    private val preferencesManager = PreferencController(application)
    val items = mutableStateListOf<PasswordEntry>()

    var responseData = mutableStateOf<ResponsData>(ResponsData("not written", secret = "not written"))

    init {
        // Load items from local storage
        loadItems()
    }

    fun setResponseData (_responsData: ResponsData) {
        responseData.value = _responsData
    }

    fun getResponseData () : ResponsData {
        Log.d("Store", responseData.value.check)
        return responseData.value
    }

    fun addItems(itmes: List<PasswordEntry>) {
        for (ele in itmes)  {
            addItem(ele)
        }
    }

    fun addItem(item: PasswordEntry) {
        items.add(item)
        Log.d("item", item.password)
        // Save items to local storage
        saveItems()
    }

    fun emptyItems() {
        items.removeAll(items)
    }

    private fun saveItems() {
        viewModelScope.launch {
            preferencesManager.saveItems(items)
        }
    }

    private fun loadItems() {
        viewModelScope.launch {
            val savedItems = preferencesManager.getItems()
            items.addAll(savedItems)
        }
    }
}