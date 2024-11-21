package com.example.financial_tracker.ui.addCategory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ToggleButton
import com.example.financial_tracker.R


class AddCategoryFragment : Fragment() {
    private lateinit var categoryName: EditText
    private lateinit var categoryType: ToggleButton
    private lateinit var saveBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_add_category, container, false)
        categoryName = root.findViewById(R.id.editTextCategory)
        categoryType = root.findViewById(R.id.toggleButtonCategory)
        saveBtn = root.findViewById(R.id.buttonCategory)

        saveBtn.setOnClickListener {
            if (categoryName.text.isNotEmpty()) {
                //TODO - обработать чо там дальше
            }
        }

        return root
    }
}