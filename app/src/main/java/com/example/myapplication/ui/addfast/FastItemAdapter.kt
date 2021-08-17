package com.example.myapplication.ui.addfast

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

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
        val fastName: TextView = itemView.findViewById(R.id.fastName)
        val fastDescription: TextView = itemView.findViewById(R.id.fastDescription)
        val startFastButton: Button = itemView.findViewById(R.id.startFastButton)

        fun bind(fastItem: FastItem){
            fastName.text = fastItem.fastName
            fastDescription.text = fastItem.fastDescription
            startFastButton.setOnClickListener(View.OnClickListener {
                Log.d("demo", "onClick for fast: " + fastName.text)
            })
        }
    }



}