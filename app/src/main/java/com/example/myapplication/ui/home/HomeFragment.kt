package com.example.myapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding
import com.google.gson.Gson
import java.time.Month


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

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
                layoutManager = LinearLayoutManager(context)
                adapter = homeItemAdapter
            }
        })
        addDataSet()

        return root
    }

    private fun addDataSet(){

        var sharedPreferences =
            context?.getSharedPreferences(
                context?.getString(R.string.current_fasts), 0)
        val gson = Gson()
//        val homeItems: List<HomeItem> = gson.fromJson(
//            sharedPreferences?.getString
//                (context?.getString(R.string.current_fasts), ""),
//            ArrayList<HomeItem>().javaClass
//        )

//        homeItemAdapter.submitList(homeItems)
//        TODO: val fastList = homeItemAdapter.submitList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun findMonth(month: String): Month {
        when(month){
            "01" -> return Month.JANUARY
            "02" -> return Month.FEBRUARY
            "03" -> return Month.MARCH
            "04" -> return Month.APRIL
            "05" -> return Month.MAY
            "06" -> return Month.JUNE
            "07" -> return Month.JULY
            "08" -> return Month.AUGUST
            "09" -> return Month.SEPTEMBER
            "10" -> return Month.OCTOBER
            "11" -> return Month.NOVEMBER
            "12" -> return Month.DECEMBER
        }
        return Month.JANUARY
    }
}