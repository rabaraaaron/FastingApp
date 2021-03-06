package com.example.myapplication.ui.home

data class HomeItem(var fastName: String, var startDate: String, var fastDuration: String,
                    var endDate: String, var comments: String, val fastDescription: String
                    , var finished: Boolean, var notificationsOn: Boolean){

    fun equals(item: HomeItem):Boolean{
        return (item.fastName == fastName
                && item.startDate == startDate
                && item.fastDuration == fastDuration
                && item.endDate == endDate
                && item.comments == comments
                && item.finished == finished)
    }

    fun isAlreadyActiveFast(item: HomeItem): Boolean{
        return (item.fastName == fastName
                && item.startDate == startDate
                && item.endDate == endDate)
    }


}
