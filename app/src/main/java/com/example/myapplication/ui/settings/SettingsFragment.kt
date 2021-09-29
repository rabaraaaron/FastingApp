package com.example.myapplication.ui.settings

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R as R1
import android.R as R2
import com.example.myapplication.databinding.FragmentSettingsBinding
import com.example.myapplication.ui.extras.ExtrasItem
import com.example.myapplication.ui.home.HomeItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private var _binding: FragmentSettingsBinding? = null
    private val gson = Gson()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("UseSwitchCompatOrMaterialCode", "CommitPrefEdits")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        settingsViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })

        val sharedPreferences =
            textView.context.getSharedPreferences(
                textView.context.getString(R1.string.current_fasts), 0)
        val sharedPreferencesEditor = sharedPreferences.edit()

        val spinner: Spinner = binding.bibleVersionSpinner
        settingsViewModel.versions.observe(viewLifecycleOwner, {
            val adapter = ArrayAdapter(spinner.context, R2.layout.simple_spinner_item, it)
            spinner.adapter = adapter

            val type = object : TypeToken<String>() {}.type

            val selectedVersion: String? = gson.fromJson(
                sharedPreferences.getString
                    (spinner.context.getString(R1.string.bible_version),
                    null), type)
            if(selectedVersion != null){
                val index = it.indexOf(selectedVersion)
                spinner.setSelection(index)
            }
        })
        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                println("Selected: ${settingsViewModel.versions.value?.get(position)}")

                val serializedObject: String? = gson.toJson(
                    settingsViewModel.versions.value?.get(position))
                sharedPreferencesEditor.putString(
                    spinner.context.getString(R1.string.bible_version),
                    serializedObject).apply()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                println("Nothing is selected")
            }
        }


        val dailyNotificationSwitch = binding.dailyNotificationSwitch
        dailyNotificationSwitch.isChecked = sharedPreferences.getBoolean(
            getString(R1.string.daily_notifications_switch), false
        )
        dailyNotificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            if(!isChecked){
                val type = object : TypeToken<List<HomeItem>>() {}.type
                val homeItems: ArrayList<HomeItem> = gson.fromJson(
                    sharedPreferences.getString
                        (textView.context.getString(com.example.myapplication.R.string.current_fasts), null),
                    type,
                )
                for(item in homeItems){
                    item.notificationsOn = false
                }
                val serializedObject: String? = gson.toJson(homeItems)
                sharedPreferencesEditor.putString(
                    textView.context.getString(com.example.myapplication.R.string.current_fasts), serializedObject,
                ).apply()

            }
            sharedPreferencesEditor.putBoolean(
                getString(R1.string.daily_notifications_switch), isChecked).apply()

        }

        val resetStatsButton: ImageButton = binding.resetStatsButton

        resetStatsButton
            .setOnClickListener {

                val dialogBuilder = AlertDialog.Builder(resetStatsButton
                    .context)
                dialogBuilder
                    .setTitle("Reset Data")
                    .setMessage("Do you wish to reset all data on this app?")
                    .setCancelable(true)
                    .setPositiveButton(("Yes")) { _, _ ->

                        val typeExtras = object : TypeToken<ArrayList<ExtrasItem>>() {}.type
                        val gson = Gson()
                        val extrasItems: ArrayList<ExtrasItem>? = gson.fromJson(
                            sharedPreferences?.getString
                                (view?.context?.getString(com.example.myapplication.R.string.finished_fasts), null),
                            typeExtras,
                        )
                        extrasItems?.clear()
                        val type = object : TypeToken<List<HomeItem>>() {}.type
                        val homeItems: ArrayList<HomeItem> = gson.fromJson(
                            sharedPreferences.getString
                                (textView.context.getString(com.example.myapplication.R.string.current_fasts), null),
                            type,
                        )
                        homeItems.clear()
                        sharedPreferencesEditor.putInt(resetStatsButton
                            .context.getString(R1.string.amount_finished), 0).apply()
                        sharedPreferencesEditor.putInt(resetStatsButton
                            .context.getString(R1.string.amount_started), 0).apply()
                        var serializedObject: String? = gson.toJson(extrasItems)
                        sharedPreferencesEditor.putString(
                            view?.context?.getString(R1.string.finished_fasts), serializedObject,
                        ).apply()
                        serializedObject = gson.toJson(homeItems)
                        sharedPreferencesEditor.putString(
                            view?.context?.getString(R1.string.current_fasts), serializedObject
                        ).apply()
                    }
                val alert = dialogBuilder.create()
                alert.show()
            }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}