package com.example.financial_tracker.ui.chart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChartViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is chart Fragment"
    }
    val text: LiveData<String> = _text
}