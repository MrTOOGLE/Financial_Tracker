package com.example.financial_tracker.ui.addCategory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.ToggleButton
import com.example.financial_tracker.R
import com.example.financial_tracker.saves.Category
import com.example.financial_tracker.saves.findCategory
import com.example.financial_tracker.saves.writeCategory

/**
 * Фрагмент для добавления новых категорий пользователя
 */
class AddCategoryFragment : Fragment() {
    private lateinit var categoryName: EditText
    private lateinit var categoryType: ToggleButton
    private lateinit var saveBtn: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_add_category, container, false)
        categoryName = root.findViewById(R.id.editTextCategory)
        categoryType = root.findViewById(R.id.toggleButtonCategory)
        saveBtn = root.findViewById(R.id.buttonCategory)

        // Добавляем название файла "/categories.csv"
        val path: String = context?.filesDir.toString() + "/categories.csv"

        // Сохраняем новую категорию, созданную пользователем
        saveBtn.setOnClickListener {
            if (categoryName.text.isNotEmpty()) {
                if (findCategory(path, categoryName.text.toString()) == null) {
                    val name: String = categoryName.text.toString()
                    val type: String = categoryType.text.toString()
                    writeCategory(path, Category(name, type, false))
                    Toast.makeText(context, "Категория создана", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Такая категория уже существует", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Введите название категории", Toast.LENGTH_SHORT).show()
            }
        }

        // Показ доходов по умолчанию
        categoryType.performClick()

        return root
    }
}