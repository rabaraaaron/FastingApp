package com.example.myapplication.model

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myapplication.R
import java.util.*
import kotlin.collections.ArrayList

class MotivationalStrings(context: Context) {
    private var motivationalStrings = ArrayList<String>()



    init {
        val quotes = context.getString(R.string.quotes)
        val newline = System.getProperty("line.separator")
        val hasNewline: Boolean = quotes.contains(newline)
        println(hasNewline)
        val quotesArray = quotes.split("%")
        motivationalStrings = quotesArray as ArrayList<String>
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getRandomQuote(): String{
        val rand = Random()
        val index = rand.nextInt(103)
        return motivationalStrings[index]
    }

}

