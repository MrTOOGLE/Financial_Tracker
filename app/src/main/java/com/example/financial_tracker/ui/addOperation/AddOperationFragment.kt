package com.example.financial_tracker.ui.addOperation

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.financial_tracker.R

class AddOperationFragment : Fragment() {

    companion object {
        fun newInstance() = AddOperationFragment()
    }

    private val viewModel: AddOperationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_add_operation, container, false)
    }
}