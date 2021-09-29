package com.example.myapplication.ui.extras

import android.app.AlertDialog
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


class ExtrasItemAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var extrasItems: List<ExtrasItem> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ExtrasItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.extras_item, parent, false)
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ExtrasItemViewHolder -> {
                holder.bind(extrasItems[position])
            }
        }
    }


    override fun getItemCount(): Int {
        return extrasItems.size
    }

    fun submitList(finishedFastsList: List<ExtrasItem>){
        extrasItems = finishedFastsList
    }


    class ExtrasItemViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){

        private val fastName: TextView = itemView.findViewById(R.id.finishedFastName)
        private val informationButton: ImageButton = itemView.findViewById(R.id.finishedInformationButton)
        private val fastStartDate: TextView = itemView.findViewById(R.id.finishedFastStart)
        private val fastEndDate: TextView = itemView.findViewById(R.id.finishedFastEnd)
        private val fastDuration: TextView = itemView.findViewById(R.id.finishedFastDuration)
        private val comments: EditText = itemView.findViewById(R.id.finishedComments)


        private var sharedPreferences =
            itemView.context.getSharedPreferences(
                itemView.context.getString(R.string.current_fasts), 0)
        private var sharedPreferencesEditor = sharedPreferences.edit()


        fun bind(extrasItem: ExtrasItem){

            val dtf: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
            val startDate = LocalDate.parse(extrasItem.startDate, dtf)
            val endDate = LocalDate.parse(extrasItem.endDate, dtf)

            val initialRemainingDays = calculateRemainingDays(Period.between(startDate, endDate)).toDouble()
            val message  = extrasItem.comments

            fastName.text = extrasItem.fastName
            val dayStr = if(initialRemainingDays.toInt() == 1) "day" else "days"
            ("Start Date: " + extrasItem.startDate).also { fastStartDate.text = it }
            ("End Date: " + extrasItem.endDate).also { fastEndDate.text = it }
            "Duration: ${initialRemainingDays.toInt()} $dayStr".also { fastDuration.text = it }
            comments.setText(message)

            informationButton.setOnClickListener {
                val dialogBuilder = AlertDialog.Builder(itemView.context)

                dialogBuilder.setMessage(extrasItem.fastDescription)
                    .setCancelable(true)

                val alert = dialogBuilder.create()
                alert.setTitle("${extrasItem.fastName} Information")
                alert.show()
            }


            comments.setOnEditorActionListener { _, keyCode, event ->
                if (((event?.action ?: -1) == KeyEvent.ACTION_DOWN)
                    || keyCode == EditorInfo.IME_ACTION_DONE) {

                    val type = object : TypeToken<List<ExtrasItem>>() {}.type
                    val extrasItems: ArrayList<ExtrasItem> = Gson().fromJson(
                        sharedPreferences.getString
                            (itemView.context.getString(R.string.finished_fasts), null),
                        type,
                    )
                    for (item in extrasItems){
                        if(item.equals(extrasItem)){
                            item.comments = comments.text.toString()
                        }
                    }
                    val serializedObject: String? = Gson().toJson(extrasItems)
                    sharedPreferencesEditor.putString(
                        itemView.context.getString(R.string.finished_fasts), serializedObject,
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