package com.ather.mytimer

import android.content.Context
import android.preference.PreferenceManager


class MyPreference(context: Context?) {

    companion object{
        private const val THEME_TAG = "themeTag"
    }

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    var darkMode = preferences.getInt(THEME_TAG, 0)
        set(value) = preferences.edit().putInt(THEME_TAG, value).apply()
}