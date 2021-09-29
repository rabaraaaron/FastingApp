package com.example.myapplication.ui.addfast

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.ui.home.HomeItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class FastItemAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var fastItems: List<FastItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FastItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.fast_item_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is FastItemViewHolder -> {
                holder.bind(fastItems[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return fastItems.size
    }

    fun submitList(fastItemsList: List<FastItem>){
        fastItems = fastItemsList
    }

    class FastItemViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){

        private val context = itemView.context
        private var sharedPreferences =
            context.getSharedPreferences(
                context.getString(R.string.current_fasts), 0)
        private var sharedPreferencesEditor = sharedPreferences.edit()
        private val gson = Gson()
        private val fastName: TextView = itemView.findViewById(R.id.fastName)
        private val fastDescription: TextView = itemView.findViewById(R.id.fastDescription)
        private val startFastButton: Button = itemView.findViewById(R.id.startFastButton)
        private val datePicker:DatePicker = DatePicker(itemView.rootView.context)
        private val datePickerButton:ImageButton = ImageButton(itemView.rootView.context)
        private val calendarView: View = LayoutInflater.from(context)
            .inflate(R.layout.alert_dialog_view, null)
        private val commentsEditText: EditText = calendarView.findViewById(R.id.commentsTextArea)
        private val startingOnStr: TextView = calendarView.findViewById(R.id.startingOn)
        private val endingOnStr = calendarView.findViewById<TextView>(R.id.endingOn)
        var switch = -1
        private val defaultStartingOn = "Starting on:"
        private val defaultEndingOn = "Ending on:"



        fun bind(fastItem: FastItem){
            fastName.text = fastItem.fastName
            fastDescription.text = fastItem.fastDescription
            datePickerButton.setImageResource(R.drawable.ic_baseline_date_range_24)


            startFastButton.setOnClickListener {
                val alertDialog = AlertDialog.Builder(itemView.rootView.context)
                val datePickerAlertDialog = AlertDialog.Builder(itemView.rootView.context)


                datePickerAlertDialog.setView(datePicker)
                    .setNegativeButton(
                        "Cancel",
                    ) { dialog, _ ->
                        dialog.dismiss()
                        val datePickerParent = datePicker.parent as ViewGroup
                        datePickerParent.removeView(datePicker)

                    }
                    .setPositiveButton(
                        "Select",
                    ) { dialog, _ ->
                        dialog.dismiss()
                        var month = datePicker.month.toString()
                        month = if (month.toInt() + 1 < 10) {
                            "0${month.toInt() + 1}"
                        } else {
                            "${month.toInt() + 1}"
                        }
                        val year = datePicker.year
                        var day = datePicker.dayOfMonth.toString()
                        if (day.toInt() < 10) {
                            day = "0$day"
                        }

                        if (switch == 0) {
                            "$defaultStartingOn $month/$day/$year".also {
                                startingOnStr.text = it
                            }
                        } else if (switch == 1) {
                            "$defaultEndingOn $month/$day/$year".also {
                                endingOnStr.text = it
                            }
                        }

                        val datePickerParent = datePicker.parent as ViewGroup
                        datePickerParent.removeView(datePicker)
                    }
                    .setCancelable(false)

                alertDialog.setView(calendarView)

                val firstCalendarButton =
                    calendarView.findViewById<ImageButton>(R.id.calendarButton1)
                val secondCalendarButton =
                    calendarView.findViewById<ImageButton>(R.id.calendarButton2)

                firstCalendarButton.setOnClickListener {
                    datePickerAlertDialog.show()
                    switch = 0
                }
                secondCalendarButton.setOnClickListener {
                    datePickerAlertDialog.show()
                    switch = 1
                }

                alertDialog.setTitle(fastName.text)
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.cancel()
                        val calendarViewParent = calendarView.parent as ViewGroup
                        calendarViewParent.removeView(calendarView)

                    }
                    .setPositiveButton("Confirm") { dialog, _ ->
                        val comment = commentsEditText.text
                        dialog.dismiss()
                        val calendarViewParent = calendarView.parent as ViewGroup
                        calendarViewParent.removeView(calendarView)

                        if (startingOnStr.text.length > 12 && endingOnStr.text.length > 10) {
                            if (!sharedPreferences.contains(context.getString(R.string.current_fasts))) {

                                val homeItems = ArrayList<HomeItem>()
                                val period = changeDateFormat(
                                    startingOnStr.text as String,
                                    endingOnStr.text as String,
                                )
                                val homeItem = HomeItem(
                                    startDate = startingOnStr.text.substring(
                                        13,
                                        startingOnStr.text.length,
                                    ),
                                    fastName = fastName.text as String,
                                    fastDuration = period,
                                    endDate = endingOnStr.text.substring(
                                        11,
                                        endingOnStr.text.length,
                                    ),
                                    comments = comment.toString(),
                                    fastDescription = fastItem.fastDescription,
                                    finished = false,
                                    notificationsOn = false
                                )
                                homeItems.add(homeItem)
                                val serializedObject: String? = gson.toJson(homeItems)
                                sharedPreferencesEditor.putString(
                                    context.getString
                                        (R.string.current_fasts),
                                    serializedObject,
                                ).commit()

                                var amountStarted = sharedPreferences.getInt(
                                    context.getString(R.string.amount_started), 0)
                                amountStarted++
                                sharedPreferencesEditor.putInt(context.getString
                                    (R.string.amount_started), amountStarted).commit()
                                println("Added to amount started")

                                startingOnStr.text = defaultStartingOn
                                endingOnStr.text = defaultEndingOn
                            } else {
                                val period = changeDateFormat(
                                    startingOnStr.text as String,
                                    endingOnStr.text as String,
                                )

                                val type = object : TypeToken<List<HomeItem>>() {}.type
                                val homeItems: ArrayList<HomeItem> = gson.fromJson(
                                    sharedPreferences.getString
                                        (context.getString(R.string.current_fasts), null),
                                    type,
                                )

                                var amountStarted = sharedPreferences.getInt(
                                    context.getString(R.string.amount_started), 0)
                                amountStarted++
                                sharedPreferencesEditor.putInt(context.getString
                                    (R.string.amount_started), amountStarted).commit()
                                println("Added to amount started")

                                val homeItem = HomeItem(
                                    fastName = fastName.text!!.toString(),
                                    startDate = startingOnStr.text.substring(
                                        13,
                                        startingOnStr.text.length,
                                    ),
                                    endDate = endingOnStr.text.substring(
                                        11,
                                        endingOnStr.text.length,
                                    ),
                                    fastDuration = period,
                                    comments = comment.toString(),
                                    fastDescription = fastItem.fastDescription,
                                    finished = false,
                                    notificationsOn = false
                                )

                                var found = false
                                for(item in homeItems){
                                    if(item.isAlreadyActiveFast(homeItem)){
                                        found = true
                                        println("Fast is already active")
                                        break
                                    }
                                }

                                if(!found){
                                    homeItems.add(homeItem)
                                    val serializedObject: String? = gson.toJson(homeItems)
                                    sharedPreferencesEditor.putString(
                                        context.getString(R.string.current_fasts), serializedObject,
                                    ).commit()
                                }

                                for (item in homeItems) {
                                    println(
                                        "${item.fastName}, ${item.startDate}, " +
                                                "${item.fastDuration}, ${item.endDate}",
                                    )
                                }

                            }

                        }
                    }
                    .setCancelable(false)
                alertDialog.show()
            }
        }

        private fun changeDateFormat(startingDate: String, endingDate: String): String {
            var str1 = startingDate
            var str2 = endingDate

            str1 = str1.replace(" ", "", true)
            str2 = str2.replace(" ", "", true)

            str1 = str1.substring(11, str1.length)
            str2 = str2.substring(9, str2.length)

            str1 = str1.replace("/", "", true)
            str2 = str2.replace("/", "", true)

            val from = LocalDate.parse(str1, DateTimeFormatter.ofPattern("MMddyyyy"))
            val to = LocalDate.parse(str2, DateTimeFormatter.ofPattern("MMddyyyy"))
            val period = Period.between(from, to)

            return "${period.months}:${period.days}:${period.years}"
        }
    }

}