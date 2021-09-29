package com.example.myapplication.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.R

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the settings Fragment"
    }

    private val _versions = MutableLiveData<Array<String>>().apply {
        val context = getApplication<Application>().applicationContext
        value = context.resources.getStringArray(R.array.Versions)
    }

    val versions: LiveData<Array<String>> = _versions
    val text: LiveData<String> = _text
}