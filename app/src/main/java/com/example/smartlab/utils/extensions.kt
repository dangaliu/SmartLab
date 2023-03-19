package com.example.smartlab.utils

import android.content.Context
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import java.security.MessageDigest
import javax.crypto.Cipher

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "smartlab")

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}