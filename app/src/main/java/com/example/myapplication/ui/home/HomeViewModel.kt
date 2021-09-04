package com.example.myapplication.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "Go to the Add Fast menu!"
    }

    private var _activeFastList = MutableLiveData<List<HomeItem>>().apply {
        val context = getApplication<Application>().applicationContext
        val sharedPreferences =
            context.getSharedPreferences(
                context.getString(R.string.current_fasts), 0)
        val type = object : TypeToken<List<HomeItem>>() {}.type

        val homeItems: ArrayList<HomeItem> = if(Gson().fromJson<ArrayList<HomeItem>>(
                sharedPreferences.getString
                    (context.getString(R.string.current_fasts), null), type,
            ) == null){
            ArrayList()
        } else{
            Gson().fromJson(
                sharedPreferences.getString(context.getString(R.string.current_fasts), null),
                type,
            )
        }
        value = homeItems
    }

    val text: LiveData<String> = _text
    val activeFastList: LiveData<List<HomeItem>> = _activeFastList
}