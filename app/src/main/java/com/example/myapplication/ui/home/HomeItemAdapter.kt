package com.example.myapplication.ui.home

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import java.time.*
import java.time.format.DateTimeFormatter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


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

        private val fastName: TextView = itemView.findViewById(R.id.activeFastName)
        private val informationButton: ImageButton = itemView.findViewById(R.id.informationButton)
        private val fastStartDate: TextView = itemView.findViewById(R.id.activeFastStart)
        private val fastEndDate: TextView = itemView.findViewById(R.id.activeFastEnd)
        private val fastDuration: TextView = itemView.findViewById(R.id.activeFastDuration)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
        private val comments: EditText = itemView.findViewById(R.id.comments)
        private var sharedPreferences =
            itemView.context.getSharedPreferences(
                itemView.context.getString(R.string.current_fasts), 0)
        private var sharedPreferencesEditor = sharedPreferences.edit()


        fun bind(homeItem: HomeItem){

            val dtf: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
            val startDate = LocalDate.parse(homeItem.startDate, dtf)
            val endDate = LocalDate.parse(homeItem.endDate, dtf)
            val currentDate = LocalDateTime.now().toLocalDate()

            val periodLeft: Period = when {
                startDate.isBefore(currentDate) -> {
                    Period.between(currentDate, endDate)
                }
                startDate.equals(currentDate) -> {
                    Period.between(startDate, endDate)
                }
                else -> {
                    Period.between(startDate, endDate)
                }
            }

            val remainingDays = calculateRemainingDays(periodLeft).toDouble()
            val initialRemainingDays = calculateRemainingDays(Period.between(startDate, endDate)).toDouble()

            val percentageDone = (initialRemainingDays - remainingDays) / initialRemainingDays
            val message  = homeItem.comments

            fastName.text = homeItem.fastName
            ("Start Date: " + homeItem.startDate).also { fastStartDate.text = it }
            ("End Date: " + homeItem.endDate).also { fastEndDate.text = it }
            "Time Left: ${remainingDays.toInt()} days".also { fastDuration.text = it }
            comments.setText(message)
            progressBar.max = 1000
            progressBar.progress = (percentageDone*1000).toInt()


            informationButton.setOnClickListener {
                val dialogBuilder = AlertDialog.Builder(itemView.context)

                println(homeItem.fastDescription)
                dialogBuilder.setMessage(homeItem.fastDescription)
                    .setCancelable(true)

                val alert = dialogBuilder.create()
                alert.setTitle("${homeItem.fastName} Information")
                alert.show()
            }

            comments.setOnEditorActionListener { _, keyCode, event ->
                if (((event?.action ?: -1) == KeyEvent.ACTION_DOWN)
                    || keyCode == EditorInfo.IME_ACTION_DONE) {

                    val type = object : TypeToken<List<HomeItem>>() {}.type
                    val homeItems: ArrayList<HomeItem> = Gson().fromJson(
                        sharedPreferences.getString
                            (itemView.context.getString(R.string.current_fasts), null),
                        type,
                    )
                    for (item in homeItems){
                        if(item.equals(homeItem)){
                            item.comments = comments.text.toString()
                        }
                    }
                    val serializedObject: String? = Gson().toJson(homeItems)
                    sharedPreferencesEditor.putString(
                        itemView.context.getString(R.string.current_fasts), serializedObject,
                    ).commit()

                    return@setOnEditorActionListener false
                }
                return@setOnEditorActionListener false
            }
        }

        private fun calculateRemainingDays(period: Period): Int {
            var daysLeft = 0
            var years = period.years
            var months = period.months
            var days = period.days
            while (years > 0){
                daysLeft += 365
                years--
            }
            while (months > 0){
                daysLeft += 30
                months--
            }
            while (days > 0){
                daysLeft += days
                days = 0
            }
            return daysLeft
        }

    }
}