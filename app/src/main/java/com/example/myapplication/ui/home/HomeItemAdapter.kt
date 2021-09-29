package com.example.myapplication.ui.home

import android.app.*
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.ReminderBroadcast
import com.example.myapplication.ui.extras.ExtrasItem
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
        private val notificationButton: ImageButton = itemView.findViewById(R.id.notificationBell)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
        private val fastStartDate: TextView = itemView.findViewById(R.id.activeFastStart)
        private val fastEndDate: TextView = itemView.findViewById(R.id.activeFastEnd)
        private val fastDuration: TextView = itemView.findViewById(R.id.activeFastDuration)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
        private val finishedButton: ImageButton = itemView.findViewById(R.id.finishedButton)
        private val comments: EditText = itemView.findViewById(R.id.comments)
        private var sharedPreferences =
            itemView.context.getSharedPreferences(
                itemView.context.getString(R.string.current_fasts), 0)
        private var sharedPreferencesEditor = sharedPreferences.edit()
        private val gson: Gson = Gson()


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

            if(remainingDays.toInt() != 0){
                finishedButton.setImageResource(0)
            }

            finishedButton.setOnClickListener {

                val dialogBuilder = AlertDialog.Builder(itemView.context)
                dialogBuilder.setMessage(homeItem.fastDescription)
                    .setTitle("Great Job!")
                    .setMessage("Did you successfully complete this fast?")
                    .setCancelable(true)
                    .setPositiveButton(("Yes")) { _, _ ->
                        val typeExtras = object : TypeToken<ArrayList<ExtrasItem>>() {}.type
                        var extrasItems: ArrayList<ExtrasItem>? = gson.fromJson(
                            sharedPreferences.getString
                                (itemView.context.getString(R.string.finished_fasts), null),
                            typeExtras,
                        )
                        var found = false
                        if(extrasItems != null){
                            for (item in extrasItems){
                                if(item.equals(homeItem)){
                                    found = true
                                    break
                                }
                            }
                        } else{
                            extrasItems = ArrayList()
                        }

                        if(!found){
                            extrasItems.add(
                                ExtrasItem(
                                    fastName = homeItem.fastName,
                                    startDate = homeItem.startDate,
                                    endDate = homeItem.endDate,
                                    fastDuration = remainingDays.toString(),
                                    comments = homeItem.comments,
                                    fastDescription = homeItem.fastDescription
                                )
                            )
                            val serializedObject: String? = gson.toJson(extrasItems)
                            sharedPreferencesEditor.putString(
                                itemView.context.getString(R.string.finished_fasts), serializedObject,
                            ).commit()
                        }

                        var amountFinished = sharedPreferences.getInt(itemView.context.getString
                            (R.string.amount_finished), 0)
                        amountFinished++
                        sharedPreferencesEditor.putInt(itemView.context.getString
                            (R.string.amount_finished), amountFinished).commit()
                        println("Added to amount finished")

                        val type = object : TypeToken<List<HomeItem>>() {}.type
                        val homeItems: ArrayList<HomeItem> = gson.fromJson(
                            sharedPreferences.getString
                                (itemView.context.getString(R.string.current_fasts), null),
                            type,
                        )
                        for(item in homeItems){
                            if(item.equals(homeItem)){
                                item.finished = true
                                homeItems.remove(item)
                                break
                            }
                        }
                        val serializedObject: String? = gson.toJson(homeItems)
                        sharedPreferencesEditor.putString(
                            itemView.context.getString(R.string.current_fasts), serializedObject,
                        ).commit()

                        val vg = itemView.findViewById(R.id.homeMainLayout) as ViewGroup
                        vg.removeAllViews()
                        vg.refreshDrawableState()

                    }.setNegativeButton(("No")){ _, _ ->
                        val type = object : TypeToken<List<HomeItem>>() {}.type
                        val homeItems: ArrayList<HomeItem> = gson.fromJson(
                            sharedPreferences.getString
                                (itemView.context.getString(R.string.current_fasts), null),
                            type,
                        )
                        for(item in homeItems){
                            if(item.equals(homeItem)){
                                homeItems.remove(item)
                                break
                            }
                        }
                        val serializedObject: String? = gson.toJson(homeItems)
                        sharedPreferencesEditor.putString(
                            itemView.context.getString(R.string.current_fasts), serializedObject,
                        ).commit()

                        val vg = itemView.findViewById(R.id.homeMainLayout) as ViewGroup
                        vg.removeAllViews()
                        vg.refreshDrawableState()
                    }
                val alert = dialogBuilder.create()
                alert.show()
            }

            val percentageDone = (initialRemainingDays - remainingDays) / initialRemainingDays
            val message  = homeItem.comments

            fastName.text = homeItem.fastName
            val dayStr = if(remainingDays.toInt() == 1) "day" else "days"
            ("Start Date: " + homeItem.startDate).also { fastStartDate.text = it }
            ("End Date: " + homeItem.endDate).also { fastEndDate.text = it }
            "Time Left: ${remainingDays.toInt()} $dayStr".also { fastDuration.text = it }
            comments.setText(message)
            progressBar.max = 1000
            progressBar.progress = (percentageDone*1000).toInt()


            informationButton.setOnClickListener {
                val dialogBuilder = AlertDialog.Builder(itemView.context)

                dialogBuilder.setMessage(homeItem.fastDescription)
                    .setCancelable(true)

                val alert = dialogBuilder.create()
                alert.setTitle("${homeItem.fastName} Information")
                alert.show()
            }

            deleteButton.setOnClickListener {
                val dialogBuilder = AlertDialog.Builder(itemView.context)

                dialogBuilder.setMessage(homeItem.fastDescription)
                    .setTitle("Delete this fast?")
                    .setMessage("${homeItem.fastName}\n${homeItem.startDate} - ${homeItem.endDate}")
                    .setCancelable(true)
                    .setPositiveButton(("Delete")) { _, _ ->
                        val type = object : TypeToken<List<HomeItem>>() {}.type
                        val homeItems: ArrayList<HomeItem> = gson.fromJson(
                            sharedPreferences.getString
                                (itemView.context.getString(R.string.current_fasts), null),
                            type,
                        )
                        homeItems.remove(homeItem)
                        val serializedObject: String? = gson.toJson(homeItems)
                        sharedPreferencesEditor.putString(
                            itemView.context.getString(R.string.current_fasts), serializedObject,
                        ).commit()

                        val vg = itemView.findViewById(R.id.homeMainLayout) as ViewGroup
                        vg.removeAllViews()
                        vg.refreshDrawableState()

                    }
                val alert = dialogBuilder.create()
                alert.show()

            }

            comments.setOnEditorActionListener { _, keyCode, event ->
                if (((event?.action ?: -1) == KeyEvent.ACTION_DOWN)
                    || keyCode == EditorInfo.IME_ACTION_DONE) {

                    val type = object : TypeToken<List<HomeItem>>() {}.type
                    val homeItems: ArrayList<HomeItem> = gson.fromJson(
                        sharedPreferences.getString
                            (itemView.context.getString(R.string.current_fasts), null),
                        type,
                    )
                    for (item in homeItems){
                        if(item.equals(homeItem)){
                            item.comments = comments.text.toString()
                        }
                    }
                    val serializedObject: String? = gson.toJson(homeItems)
                    sharedPreferencesEditor.putString(
                        itemView.context.getString(R.string.current_fasts), serializedObject,
                    ).commit()

                    return@setOnEditorActionListener false
                }
                return@setOnEditorActionListener false
            }

            if(homeItem.notificationsOn){
                notificationButton.setColorFilter(Color.argb(255, 255, 255, 255))
            }

            notificationButton.setOnClickListener {
                val type = object : TypeToken<List<HomeItem>>() {}.type
                val homeItems: ArrayList<HomeItem> = gson.fromJson(
                    sharedPreferences.getString
                        (itemView.context.getString(R.string.current_fasts), null),
                    type,
                )
                val masterNotificationSetting = sharedPreferences.getBoolean(
                    itemView.context.getString(R.string.daily_notifications_switch), false
                )
                if(masterNotificationSetting){
                    for(item in homeItems){
                        if(item.equals(homeItem)){
                            item.notificationsOn = !item.notificationsOn
                            if(item.notificationsOn){
                                //TODO: display correct fasting message for notifications
                                    //TODO: schedule the notifications every day until completion

                                Toast.makeText(itemView.context, "Setting daily reminders for" +
                                        " ${item.fastName}", Toast.LENGTH_SHORT).show()

                                val intent = Intent(itemView.context, ReminderBroadcast::class.java)
                                val pendingIntent = PendingIntent.getBroadcast(itemView.context,
                                    0, intent, 0)
                                val alarmManager: AlarmManager = itemView.context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                                val timeAtButtonClick = System.currentTimeMillis()
                                val oneSecond = 1000 * 1

                                alarmManager.set(AlarmManager.RTC_WAKEUP,
                                    timeAtButtonClick + oneSecond, pendingIntent)

                            } else{
                                Toast.makeText(itemView.context, "Notifications OFF for: " +
                                        item.fastName, Toast.LENGTH_SHORT).show()
                            }
                            val myViewGroup = itemView.findViewById(R.id.homeItemLayout) as ViewGroup
                            myViewGroup.removeViewAt(1)
                            if(item.notificationsOn){
                                notificationButton.setColorFilter(Color.argb(255, 255, 255, 255))
                            } else{
                                notificationButton.setColorFilter(Color.argb(255, 0, 0, 0))
                            }
                            myViewGroup.addView(notificationButton, 1)
                            val serializedObject: String? = gson.toJson(homeItems)
                            sharedPreferencesEditor.putString(
                                itemView.context.getString(R.string.current_fasts), serializedObject,
                            ).commit()
                            break
                        }
                    }
                } else{
                    Toast.makeText(itemView.context, "Enable Notifications in Settings",
                        Toast.LENGTH_SHORT).show()
                }
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