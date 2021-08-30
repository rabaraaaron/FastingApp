package com.example.myapplication.ui.addfast

import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
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

        private var sharedPreferences =
            itemView.context.getSharedPreferences(
                itemView.context.getString(R.string.current_fasts), 0)
        private var sharedPreferencesEditor = sharedPreferences.edit()
        private val gson = Gson()

        private val fastName: TextView = itemView.findViewById(R.id.fastName)
        private val fastDescription: TextView = itemView.findViewById(R.id.fastDescription)
        private val startFastButton: Button = itemView.findViewById(R.id.startFastButton)
        private val datePicker:DatePicker = DatePicker(itemView.rootView.context)
        private val datePickerButton:ImageButton = ImageButton(itemView.rootView.context)
        val calendarView = LayoutInflater.from(itemView.context)
            .inflate(R.layout.alert_dialog_view, null)
        val startingOnStr = calendarView.findViewById<TextView>(R.id.startingOn)
        val endingOnStr = calendarView.findViewById<TextView>(R.id.endingOn)
        var switch = -1
        private val defaultStartingOn = "Starting on:"
        private val defaultEndingOn = "Ending on:"


        fun bind(fastItem: FastItem){
            fastName.text = fastItem.fastName
            fastDescription.text = fastItem.fastDescription
            datePickerButton.setImageResource(R.drawable.ic_baseline_date_range_24)

            startFastButton.setOnClickListener(View.OnClickListener {
                Log.d("demo", "onClick for fast: " + fastName.text)
                val alertDialog = AlertDialog.Builder(itemView.rootView.context)
                val datePickerAlertDialog = AlertDialog.Builder(itemView.rootView.context)

                datePickerAlertDialog.setView(datePicker)
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener{
                        dialog, id -> dialog.dismiss()
                        val datePickerParent = datePicker.parent as ViewGroup
                        datePickerParent.removeView(datePicker)

                    })
                    .setPositiveButton("Select", DialogInterface.OnClickListener{
                        dialog, id -> dialog.dismiss()
                        var month = datePicker.month.toString()
                        if(month.toInt() < 10){
                            month = "0$month"
                        }
                        var year = datePicker.year
                        var day = datePicker.dayOfMonth.toString()
                        if (day.toInt() < 10){
                            day = "0$day"
                        }
                        Log.d("demo","Day: $day, Month: $month, Year: $year")

                        if(switch == 0){
                            startingOnStr.text = "$defaultStartingOn $month/$day/$year"
                        } else if(switch == 1){
                            endingOnStr.text = "$defaultEndingOn $month/$day/$year"
                        }

                        val datePickerParent = datePicker.parent as ViewGroup
                        datePickerParent.removeView(datePicker)
                    })
                    .setCancelable(false)

                alertDialog.setView(calendarView)

                val firstCalendarButton = calendarView.findViewById<ImageButton>(R.id.calendarButton1)
                val secondCalendarButton = calendarView.findViewById<ImageButton>(R.id.calendarButton2)

                firstCalendarButton.setOnClickListener {
                    datePickerAlertDialog.show()
                    switch = 0
                }
                secondCalendarButton.setOnClickListener {
                    datePickerAlertDialog.show()
                    switch = 1
                }

                alertDialog.setTitle(fastName.text)
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                        val calendarViewParent = calendarView.parent as ViewGroup
                        calendarViewParent.removeView(calendarView)

                    })
                    .setPositiveButton("Confirm", DialogInterface.OnClickListener{
                        dialog, id -> dialog.dismiss()
                        val calendarViewParent = calendarView.parent as ViewGroup
                        calendarViewParent.removeView(calendarView)

                        if(startingOnStr.text.length > 12 && endingOnStr.text.length > 10){
                            if(!sharedPreferences.contains(itemView.context.getString(R.string.current_fasts))){
                                var fastMap = HashMap<String, Int>()

                                var daysBetween = changeDateFormat(startingOnStr.text as String, endingOnStr.text as String)
                                var fastID = "$fastName:${startingOnStr.text}"
                                fastMap.put(fastID, daysBetween)
                                var serializedObject: String? = gson.toJson(fastMap)
                                Log.d("Date difference: ", "$daysBetween")
                                startingOnStr.text = defaultStartingOn
                                endingOnStr.text = defaultEndingOn
                                //TODO: save the date and its values to the storage

                            }
                        }

                    })
                    .setCancelable(false)

                alertDialog.show()
            })
        }

        fun changeDateFormat(startingDate: String, endingDate: String): Int {
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
            println("The difference between " + from.format(DateTimeFormatter.ISO_LOCAL_DATE)
                    + " and " + to.format(DateTimeFormatter.ISO_LOCAL_DATE) + " is "
                    + period.getYears() + " years, " + period.getMonths() + " months and "
                    + period.getDays() + " days")
            return period.days
        }
    }

}