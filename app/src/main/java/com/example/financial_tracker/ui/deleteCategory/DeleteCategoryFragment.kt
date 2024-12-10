package com.example.financial_tracker.ui.deleteCategory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import android.widget.ToggleButton
import com.example.financial_tracker.R
import com.example.financial_tracker.saves.deleteCategoryFromCSV
import com.example.financial_tracker.saves.readCategories

/**
 * Фрагмент для удаления категорий пользователя
 */
class DeleteCategoryFragment : Fragment() {
    private lateinit var deleteBtn: Button
    private lateinit var spinner: Spinner
    private lateinit var toggleBtn: ToggleButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_delete_category, container, false)
        deleteBtn = root.findViewById(R.id.buttonDeleteCategory)
        spinner = root.findViewById(R.id.spinnerDeleteCategory)
        toggleBtn = root.findViewById(R.id.toggleButtonDeleteCategory)

        // Отображение категорий по типу: доходы/расходы для дальнейшего удаления выбранной категории
        toggleBtn.setOnClickListener {
            val categories = readCategories(context?.filesDir.toString() + "/categories.csv")
            if (toggleBtn.isChecked) {
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
            }
        }

        // Показ доходов по умолчанию
        toggleBtn.performClick()

        // Удаляем категорию
        deleteBtn.setOnClickListener {
            val deletedCategory = spinner.selectedItem.toString()
            deleteCategoryFromCSV(context?.filesDir.toString() + "/categories.csv", deletedCategory)
            Toast.makeText(context, "Категория $deletedCategory удалена", Toast.LENGTH_SHORT).show()
        }

        return root
    }
}