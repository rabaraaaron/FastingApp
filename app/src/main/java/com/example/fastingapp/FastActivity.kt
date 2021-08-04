package com.example.fastingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

class FastActivity : AppCompatActivity() {

    private var partialFastString = "A Partial Fasting is when you refrain from eating sun up to " +
            "sundown. You can also do this from a certain time of day, for example, from 7:00 am to" +
            " 4:00 pm."
    private var danielFastString = "The Daniel Fast is taken from when Daniel fasted in the Bible. " +
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

    private var completeFastString = "A complete fast is where you only drink water. You would not " +
            "eat any solid foods. Some people choose to drink water and juice to help maintain some " +
            "energy."
    private var absoluteFastString = "An absolute fast is based on both Paul and Esther’s fasts in " +
            "the Bible. You do not eat or drink during this fast. No drinking, including water. " +
            "It would be recommended that you do this fast for a short period of time as well."
    private var sexualFastString = "The concept of sexual fasting comes from Paul. He poses the idea " +
            "that through mutual consent you would be apart for a time to devote yourself " +
            "to praying. This fast would be done in the context of marriage and where both parties " +
            "are in mutual agreement."
    private var corporateFastString = "Corporate fasting can be done in a variety of different ways." +
            " You could do a corporate fast with your church, a Bible study group, your spouse, " +
            "and so on. Your group can decide what kind of fast you will be doing and for how long."
    private var soulFastString = "Soul fasting is where you obtain from a certain area of your " +
            "life that may be out of balance or something you consume much of your time.\n" +
            "\n" +
            "Ideas for soul fasting include:\n" +
            "\n" +
            "Television\n" +
            "Social Media\n" +
            "Podcasts\n" +
            "Magazines"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fast_menu)

        val partialFastButton = findViewById<ImageButton>(R.id.partialFastInfo)
        val partialFastText = findViewById<TextView>(R.id.partialFastText)
        findViewById<Button>(R.id.fastbutton1).setOnClickListener{
            val intent = Intent(this, PartialFastActivity::class.java)
            startActivity(intent)
        }

        val danielFastButton = findViewById<ImageButton>(R.id.danielFastInfo)
        val danielFastText = findViewById<TextView>(R.id.danielFastText)
        findViewById<Button>(R.id.fastbutton2).setOnClickListener{
            val intent = Intent(this, DanielFastActivity::class.java)
            startActivity(intent)
        }

        val completeFastButton = findViewById<ImageButton>(R.id.completeFastInfo)
        val completeFastText = findViewById<TextView>(R.id.completeFastText)

        val absoluteFastButton = findViewById<ImageButton>(R.id.absoluteFastInfo)
        val absoluteFastText = findViewById<TextView>(R.id.absoluteFastText)

        val sexualFastButton = findViewById<ImageButton>(R.id.sexualFastInfo)
        val sexualFastText = findViewById<TextView>(R.id.sexualFastText)

        val corporateFastButton = findViewById<ImageButton>(R.id.corporateFastInfo)
        val corporateFastText = findViewById<TextView>(R.id.corporateFastText)

        val soulFastButton = findViewById<ImageButton>(R.id.soulFastInfo)
        val soulFastText = findViewById<TextView>(R.id.soulFastText)

        var buttonTextHash = HashMap<ImageButton, Pair<TextView, String>>()
        buttonTextHash.put(partialFastButton, Pair(partialFastText, partialFastString))
        buttonTextHash.put(danielFastButton, Pair(danielFastText, danielFastString))
        buttonTextHash.put(completeFastButton, Pair(completeFastText, completeFastString))
        buttonTextHash.put(absoluteFastButton, Pair(absoluteFastText, absoluteFastString))
        buttonTextHash.put(sexualFastButton, Pair(sexualFastText, sexualFastString))
        buttonTextHash.put(corporateFastButton, Pair(corporateFastText, corporateFastString))
        buttonTextHash.put(soulFastButton, Pair(soulFastText, soulFastString))

        for((button, pair) in buttonTextHash){
            button.setOnClickListener{
                if(pair.first.text.equals("")){
                    pair.first.text = pair.second
                } else{
                    pair.first.text = ""
                }
            }
        }

    }

}