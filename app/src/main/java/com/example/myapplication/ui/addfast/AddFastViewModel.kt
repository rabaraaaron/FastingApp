package com.example.myapplication.ui.addfast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddFastViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = ""
    }
    val text: LiveData<String> = _text

    private var _fastNames = MutableLiveData<Array<String>>().apply {
        value = arrayOf("Partial Fast", "Daniel Fast", "Complete Fast", "Absolute Fast",
            "Sexual Fast", "Corporate Fast", "Soul Fast")

    }
    val fastNames: LiveData<Array<String>> = _fastNames

}