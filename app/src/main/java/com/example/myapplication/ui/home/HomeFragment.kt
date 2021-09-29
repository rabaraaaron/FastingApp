package com.example.myapplication.ui.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding
import java.time.Month


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private var list: List<HomeItem> = ArrayList()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var homeItemAdapter: HomeItemAdapter = HomeItemAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })

        val activeFastList: RecyclerView = binding.homeList
        homeViewModel.activeFastList.observe(viewLifecycleOwner, {
            activeFastList.apply {
                list = it
                layoutManager = LinearLayoutManager(context)
                adapter = homeItemAdapter
                addDataSet()

            }
        })

        createNotificationChannel(textView.context)

        return root
    }

    private fun createNotificationChannel(context: Context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val sequence: CharSequence = "DailyNotificationsChannel"
            val description = "Channel for daily notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(context.getString(
                R.string.notifications_channel),
                sequence, importance)
            channel.description = description

            val notificationManager: NotificationManager? = ContextCompat.getSystemService(
                context, NotificationManager::class.java
            )
            notificationManager?.createNotificationChannel(channel)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addDataSet(){
        homeItemAdapter.submitList(list)
    }

}