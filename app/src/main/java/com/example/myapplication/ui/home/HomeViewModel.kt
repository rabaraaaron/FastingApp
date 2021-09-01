package com.example.myapplication.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    private var _activeFastList = MutableLiveData<List<HomeItem>>().apply {
    }

    val text: LiveData<String> = _text
    var activeFastList: LiveData<List<HomeItem>> = _activeFastList
}