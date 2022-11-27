package com.ather.mytimer

import android.content.Context
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class MyPreference(context: Context?) {

    companion object{
        private const val THEME_TAG = "themeTag"
        private const val LIST_TAG = "listTag"

    }

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    var darkMode = preferences.getInt(THEME_TAG, 0)
        set(value) = preferences.edit().putInt(THEME_TAG, value).apply()

    fun getList(): ArrayList<TaskModal>{
        val gson = Gson()
        val json = preferences.getString(LIST_TAG, null)
        val type: Type = object : TypeToken<ArrayList<TaskModal?>?>() {}.type
        if (json != null) {
            var list = gson.fromJson<Any>(json, type) as ArrayList<TaskModal>
            if (list == null) {
                // if the array list is empty
                // creating a new array list.
                list = ArrayList<TaskModal>()
            }
            return list
        }
        return ArrayList<TaskModal>()
    }

    fun setList(list: ArrayList<TaskModal>){
        val editor = preferences.edit()
        val gson = Gson()

        val json: String = gson.toJson(list)
        editor.putString(LIST_TAG, json)
        editor.apply()
    }
}