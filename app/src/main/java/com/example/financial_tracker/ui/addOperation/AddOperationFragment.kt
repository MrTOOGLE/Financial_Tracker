package com.example.financial_tracker.ui.addOperation

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.financial_tracker.R

/**
 * Фрагмент для добавления расходов/доходов
 */
class AddOperationFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_add_operation, container, false)
    }
}