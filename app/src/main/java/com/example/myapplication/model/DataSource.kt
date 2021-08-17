package com.example.myapplication.model

import com.example.myapplication.ui.addfast.FastItem

class DataSource {
    companion object{
        fun createDataSet(): ArrayList<FastItem> {
            val list = ArrayList<FastItem>()
            val partialFastStr = "A Partial Fasting is when you refrain from eating sun up to " +
                    "sundown. You can also do this from a certain time of day, for example, from 7:00 am to" +
                    " 4:00 pm."
            val danielFastStr = "The Daniel Fast is taken from when Daniel fasted in the Bible. " +
                    "Daniel did not eat any meat or drink any wine for three weeks. \n\nWhat you can eat:\n" +
                    "Fruits and nuts\n" +
                    "Vegetables\n" +
                    "Water (in order to flush out toxins)\n\n" +
                    "What you cannot eat:\n" +
                    "Meats\n" +
                    "Pastries\n" +
                    "Chips\n" +
                    "Bread\n" +
                    "Fried food\n" +
                    "Coffee & Tea\n" +
                    "Juice (optional: some people do choose to drink juice during this fast)"
            val completeFastStr = "A complete fast is where you only drink water. You would not " +
                    "eat any solid foods. Some people choose to drink water and juice to help maintain some " +
                    "energy."
            val absoluteFastStr = "An absolute fast is based on both Paul and Esther’s fasts in " +
                    "the Bible. You do not eat or drink during this fast. No drinking, including water. " +
                    "It would be recommended that you do this fast for a short period of time as well."
            val sexualFastStr = "The concept of sexual fasting comes from Paul. He poses the idea " +
                    "that through mutual consent you would be apart for a time to devote yourself " +
                    "to praying. This fast would be done in the context of marriage and where both parties " +
                    "are in mutual agreement."
            val corporateFastStr = "Corporate fasting can be done in a variety of different ways." +
                    " You could do a corporate fast with your church, a Bible study group, your spouse, " +
                    "and so on. Your group can decide what kind of fast you will be doing and for how long."
            val soulFastStr = "Soul fasting is where you obtain from a certain area of your " +
                    "life that may be out of balance or something you consume much of your time.\n" +
                    "\n" +
                    "Ideas for soul fasting include:\n" +
                    "\n" +
                    "Television\n" +
                    "Social Media\n" +
                    "Podcasts\n" +
                    "Magazines"


            list.add(FastItem("Partial Fast", partialFastStr))
            list.add(FastItem("Daniel Fast", danielFastStr))
            list.add(FastItem("Complete Fast", completeFastStr))
            list.add(FastItem("Absolute Fast", absoluteFastStr))
            list.add(FastItem("Sexual Fast", sexualFastStr))
            list.add(FastItem("Corporate Fast", corporateFastStr))
            list.add(FastItem("Soul Fast", soulFastStr))


            return list
        }
    }
}