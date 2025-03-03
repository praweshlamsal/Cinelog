package com.example.cinelog.ui.history.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cinelog.databinding.ItemHistoryBinding
import com.example.cinelog.model.HistoryEvent

class HistoryAdapter(private val historyEvents: List<HistoryEvent>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    // ViewHolder class to hold the item view and bind views using ViewBinding
    inner class HistoryViewHolder(val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {

        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val historyEvent = historyEvents[position]
        holder.binding.tvAction.text = historyEvent.actionPerformed
        holder.binding.tvMovieTitle.text = historyEvent.movieName
    }

    override fun getItemCount(): Int = historyEvents.size
}

