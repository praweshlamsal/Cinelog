package com.example.cinelog.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OnboardingViewModel : ViewModel() {
    private val _currentPage = MutableLiveData(0)
    val currentPage: LiveData<Int> = _currentPage

    private val _navigationEvent = MutableLiveData<Boolean>()
    val navigationEvent: LiveData<Boolean> = _navigationEvent

    private val totalPages = 3

    fun updatePage(position: Int) {
        _currentPage.value = position
    }

    fun completeOnboarding() {
        _navigationEvent.value = true
    }

    fun getNextPage(): Int {
        val current = _currentPage.value ?: 0
        return if (current < totalPages - 1) {
            val nextPage = current + 1
            _currentPage.value = nextPage
            nextPage
        } else {
            completeOnboarding()
            current // return current to avoid crash
        }
    }
}
