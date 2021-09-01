package com.example.myapplication.ui.home

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.google.gson.Gson
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class HomeItemAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var homeItems: List<HomeItem> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HomeItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.home_item_row, parent, false)
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is HomeItemViewHolder -> {
                holder.bind(homeItems[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return homeItems.size
    }

    fun submitList(fastItemsList: List<HomeItem>){
        homeItems = fastItemsList
    }

    class HomeItemViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){

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
        val calendarView = LayoutInflater.from(context)
            .inflate(R.layout.alert_dialog_view, null)
        val startingOnStr = calendarView.findViewById<TextView>(R.id.startingOn)
        val endingOnStr = calendarView.findViewById<TextView>(R.id.endingOn)
        var switch = -1
        private val defaultStartingOn = "Starting on:"
        private val defaultEndingOn = "Ending on:"


        fun bind(homeItem: HomeItem){
            fastName.text = homeItem.fastName
            fastDescription.text = homeItem.startDate
            datePickerButton.setImageResource(R.drawable.ic_baseline_date_range_24)

            startFastButton.setOnClickListener {
                val alertDialog = AlertDialog.Builder(itemView.rootView.context)
                val datePickerAlertDialog = AlertDialog.Builder(itemView.rootView.context)

                datePickerAlertDialog.setView(datePicker)
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                        val datePickerParent = datePicker.parent as ViewGroup
                        datePickerParent.removeView(datePicker)

                    }
                    .setPositiveButton("Select") { dialog, _ ->
                        dialog.dismiss()
                        var month = datePicker.month.toString()
                        if (month.toInt() < 10) {
                            month = "0$month"
                        }
                        val year = datePicker.year
                        var day = datePicker.dayOfMonth.toString()
                        if (day.toInt() < 10) {
                            day = "0$day"
                        }

                        if (switch == 0) {
                            "$defaultStartingOn $month/$day/$year".also { startingOnStr.text = it }
                        } else if (switch == 1) {
                            "$defaultEndingOn $month/$day/$year".also { endingOnStr.text = it }
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
                        dialog.dismiss()
                        val calendarViewParent = calendarView.parent as ViewGroup
                        calendarViewParent.removeView(calendarView)

                        if (startingOnStr.text.length > 12 && endingOnStr.text.length > 10) {
                            if (!sharedPreferences.contains(context.getString(R.string.current_fasts))) {

                                val fastMap = HashMap<String, String>()

                                val period = changeDateFormat(
                                    startingOnStr.text as String,
                                    endingOnStr.text as String
                                )
                                val fastID = "${fastName.text}-${startingOnStr.text}"
                                fastMap[fastID] = "${period.first}:${period.second}"
                                val serializedObject: String? = gson.toJson(fastMap)
                                sharedPreferencesEditor.putString(
                                    context.getString
                                        (R.string.current_fasts), serializedObject
                                ).commit()

//                                Log.d("Date difference: ", " ${period.first} months and " +
//                                        "${period.second} days")
                                startingOnStr.text = defaultStartingOn
                                endingOnStr.text = defaultEndingOn
                                //TODO: save the date and its values to the storage when fast list is empty
                            } else {
                                //TODO: case for when the fast list exists

                                val period = changeDateFormat(
                                    startingOnStr.text as String,
                                    endingOnStr.text as String
                                )
                                val fastID = "${fastName.text}-${startingOnStr.text}"

                                val fastMap: HashMap<String, String> = gson.fromJson(
                                    sharedPreferences.getString
                                        (context.getString(R.string.current_fasts), ""),
                                    HashMap<String, String>().javaClass
                                )
                                fastMap[fastID] = "${period.first}:${period.second}"
                                val serializedObject: String? = gson.toJson(fastMap)
                                sharedPreferencesEditor.putString(
                                    context.getString
                                        (R.string.current_fasts), serializedObject
                                ).commit()

//                                Log.d("Fast list: ", "${fastMap.keys.size} entries exist")

                                val listOfKeys = fastMap.keys.toList()
                                for (x in listOfKeys) {
                                    println("$x, ${fastMap[x].toString()}")
                                }
                            }
                        }

                    }
                    .setCancelable(false)
                alertDialog.show()
            }
        }

        private fun changeDateFormat(startingDate: String, endingDate: String): Pair<String, String> {
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
//            println("The difference between " + from.format(DateTimeFormatter.ISO_LOCAL_DATE)
//                    + " and " + to.format(DateTimeFormatter.ISO_LOCAL_DATE) + " is "
//                    + period.getYears() + " years, " + period.getMonths() + " months and "
//                    + period.getDays() + " days")
            return Pair(period.months.toString(), period.days.toString())
        }
    }

}