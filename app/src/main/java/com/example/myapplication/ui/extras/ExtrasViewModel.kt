package com.example.myapplication.ui.extras

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.R
import com.example.myapplication.ui.home.HomeItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.math.roundToLong


class ExtrasViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences: SharedPreferences =
        application.applicationContext.getSharedPreferences(
            application.applicationContext.getString(R.string.current_fasts), 0)

    private val _text = MutableLiveData<String>().apply {
        value = "This is the extras menu!!!"
    }

    private var _finishedFastList = MutableLiveData<List<ExtrasItem>>().apply {
        val context = getApplication<Application>().applicationContext


        val type = object : TypeToken<ArrayList<ExtrasItem>>() {}.type

        val extrasItems: ArrayList<ExtrasItem> = if(Gson().fromJson<ArrayList<ExtrasItem>>(
                sharedPreferences.getString
                    (context.getString(R.string.finished_fasts), null), type,
            ) == null){
            ArrayList()
        } else{
            Gson().fromJson(
                sharedPreferences.getString(context.getString(R.string.finished_fasts), null),
                type,
            )
        }
        value = extrasItems
    }

    private val statisticsFastsStarted = "${sharedPreferences.getInt(
        application.applicationContext.getString(R.string.amount_started), 0)}"

    private val statisticsFastsCompleted = "${sharedPreferences.getInt(
        application.applicationContext.getString(R.string.amount_finished), 0)}"



    private val statisticsCompletionPercentage =
        if(statisticsFastsCompleted.toInt() != 0){
            (statisticsFastsCompleted.toDouble() /
                    statisticsFastsStarted.toDouble()*100)}
        else {
            0.00
        }


    val textExtras: LiveData<String> = _text
    val finishedFastList: LiveData<List<ExtrasItem>> = _finishedFastList
    val fastsStarted: String = statisticsFastsStarted
    val fastsCompleted: String = statisticsFastsCompleted
    val percentageCompleted: Double = statisticsCompletionPercentage
}