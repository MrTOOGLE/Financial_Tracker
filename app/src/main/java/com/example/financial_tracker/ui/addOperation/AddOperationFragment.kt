package com.example.financial_tracker.ui.addOperation

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import android.widget.ToggleButton
import com.example.financial_tracker.R
import com.example.financial_tracker.saves.Operation
import com.example.financial_tracker.saves.OperationType
import com.example.financial_tracker.saves.readCategories
import com.example.financial_tracker.saves.writeOperation
import java.util.Date

/**
 * Фрагмент для добавления расходов/доходов
 */
class AddOperationFragment : Fragment() {
    private lateinit var sumOperation: EditText
    private lateinit var addBtn: Button
    private lateinit var spinner: Spinner
    private lateinit var toggleBtn: ToggleButton

    private lateinit var operationType: OperationType

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val root = inflater.inflate(R.layout.fragment_add_operation, container, false)
        spinner = root.findViewById(R.id.spinnerOperation)
        toggleBtn = root.findViewById(R.id.toggleButtonOperation)
        addBtn = root.findViewById(R.id.buttonOperation)
        sumOperation = root.findViewById(R.id.editTextOperation)

        // Отображение категорий по типу: доходы/расходы для дальнейшего удаления выбранной категории
        toggleBtn.setOnClickListener {
            val categories = readCategories(context?.filesDir.toString() + "/categories.csv")
            if (toggleBtn.text.toString() == getString(R.string.button_income)) {
                val incomeCategory = categories
                    .filter { it.type.contains(getString(R.string.button_income), ignoreCase = true) }
                    .map { it.name }

                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    incomeCategory
                )

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
                operationType = OperationType.INCOME
            } else {
                val expensesCategory = categories
                    .filter { it.type.contains(getString(R.string.button_expenses), ignoreCase = true) }
                    .map { it.name }

                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    expensesCategory
                )

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
                operationType = OperationType.EXPENSE
            }
        }

        // Показ доходов по умолчанию
        toggleBtn.performClick()

        // Сохранение операции
        addBtn.setOnClickListener {
            if (sumOperation.text.toString().isNotEmpty()) {
                val text: Double = sumOperation.text.toString().toDouble()
                val date: String = SimpleDateFormat("dd/M/yyyy").format(Date()).toString()

                val operation: Operation = Operation("", date, text, spinner.selectedItem.toString(), operationType, "")
                writeOperation(context?.filesDir.toString() + "/operations.csv", operation)
                Toast.makeText(context, "Операция добавлена", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }
}