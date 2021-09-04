package com.example.myapplication.ui.addfast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.FragmentAddFastBinding
import com.example.myapplication.model.DataSource

class AddFastFragment : Fragment() {

    private lateinit var addFastViewModel: AddFastViewModel
    private var _binding: FragmentAddFastBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var fastItemAdapter: FastItemAdapter = FastItemAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addDataSet()
        addFastViewModel =
            ViewModelProvider(this).get(AddFastViewModel::class.java)

        _binding = FragmentAddFastBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        addFastViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })

        val fastList: RecyclerView = binding.fastList
        addFastViewModel.fastNames.observe(viewLifecycleOwner, {
            fastList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = fastItemAdapter
            }
        })
        return root
    }

    private fun addDataSet(){
        val data = DataSource.createDataSet()
        fastItemAdapter.submitList(data)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}