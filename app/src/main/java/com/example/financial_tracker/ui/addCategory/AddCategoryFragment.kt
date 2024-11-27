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
import com.example.financial_tracker.saves.writeCategory

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

        saveBtn.setOnClickListener {
            if (categoryName.text.isNotEmpty()) {
                val name: String = categoryName.text.toString()
                val type: String = categoryType.text.toString()
                // Добавляем название файла "/categories.csv"
                writeCategory(context?.filesDir.toString() + "/categories.csv", Category(name, type, false))
                Toast.makeText(context, "Категория была добавлена", Toast.LENGTH_SHORT).show()
            }
        }
        return root
    }
}