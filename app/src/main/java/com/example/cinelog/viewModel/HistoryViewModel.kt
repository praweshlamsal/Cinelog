package com.example.cinelog.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cinelog.model.HistoryEvent

class HistoryViewModel: ViewModel() {
    private val _historyEvents = MutableLiveData<List<HistoryEvent>>()
    val historyEvents: LiveData<List<HistoryEvent>> get() = _historyEvents
    init {
        // Simulating fetching data
        _historyEvents.value = listOf(
            HistoryEvent("Movie Added", "Inception"),
            HistoryEvent("Movie Deleted", "The Matrix")
        )
    }
}