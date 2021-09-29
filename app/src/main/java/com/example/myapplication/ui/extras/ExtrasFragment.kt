package com.example.myapplication.ui.extras

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentExtrasBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class ExtrasFragment : Fragment() {

    private lateinit var extrasViewModel: ExtrasViewModel
    private var _binding: FragmentExtrasBinding? = null
    private var list: List<ExtrasItem> = ArrayList()


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var extrasItemAdapter: ExtrasItemAdapter = ExtrasItemAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        extrasViewModel =
            ViewModelProvider(this).get(ExtrasViewModel::class.java)

        _binding = FragmentExtrasBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textExtras
        extrasViewModel.textExtras.observe(viewLifecycleOwner, {
            textView.text = it
        })

        val finishedFastList: RecyclerView = binding.finishedFasts
        extrasViewModel.finishedFastList.observe(viewLifecycleOwner, {
            finishedFastList.apply {
                list = it
                val layout = LinearLayoutManager(context)
                layout.orientation = RecyclerView.HORIZONTAL
                layoutManager = layout
                adapter = extrasItemAdapter
                addDataSet()
            }
        })

        val fastsStartedTV: TextView = binding.statisticsFastsStarted
        val fastsCompletedTV: TextView = binding.statisticsFastsCompleted
        val fastsPercentageTV: TextView = binding.statisticsCompletionPercentage
        "Completed fasts: \t\t\t\t\t\t\t${extrasViewModel.fastsCompleted}".also { fastsCompletedTV.text = it }
        "Started fasts: \t\t\t\t\t\t\t\t\t\t${extrasViewModel.fastsStarted}".also { fastsStartedTV.text = it }
        "${"Completion percentage: \t%.2f".format(extrasViewModel.percentageCompleted)}%".also { fastsPercentageTV.text = it }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addDataSet(){
        extrasItemAdapter.submitList(list)
    }

}