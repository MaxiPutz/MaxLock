package com.maxiputz.keychainclient.controller

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.maxiputz.keychainclient.structs.PasswordEntry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class PreferencController(context: Context) {
    private val masterKeyAlias = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "encrypted_prefs",
        masterKeyAlias,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val gson = Gson()
    private val type = object : TypeToken<List<PasswordEntry>>() {}.type

    fun saveItems(items: List<PasswordEntry>) {
        val editor = sharedPreferences.edit()
        val jsonString = gson.toJson(items, type)
        editor.putString("items_key", jsonString )
        editor.apply()
    }

    fun getItems(): List<PasswordEntry> {
        val jsonString = sharedPreferences.getString("items_key", null) ?: return emptyList()
        return gson.fromJson(jsonString, type)
    }
}