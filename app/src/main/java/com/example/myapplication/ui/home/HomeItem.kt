package com.example.myapplication.ui.home

data class HomeItem(var fastName: String, var startDate: String, var fastDuration: String,
                    var endDate: String, var comments: String, val fastDescription: String){

    fun equals(item: HomeItem):Boolean{
        return (item.fastName == fastName
                && item.startDate == startDate
                && item.fastDuration == fastDuration
                && item.endDate == endDate
                && item.comments == comments)
    }

}
