package com.example.financial_tracker.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.financial_tracker.R
import com.example.financial_tracker.databinding.FragmentHomeBinding
import android.util.Log
import android.widget.ToggleButton
import com.example.financial_tracker.saves.Operation
import com.example.financial_tracker.saves.OperationType
import com.example.financial_tracker.saves.readOperationsForWeek
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry


/**
 * Фрагмент "главной станицы", где пользователь видит основную ифнормачию о своих операциях
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var pieChart: PieChart
    private lateinit var toggleBtn: ToggleButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // -----------------------------------------------
        pieChart = root.findViewById(R.id.pieChartHome)
        toggleBtn = root.findViewById(R.id.toggleButtonHome)
        val path: String = context?.filesDir.toString() + "/operations.csv"

        toggleBtn.setOnClickListener {
            val operations: List<Operation> =
                if (toggleBtn.text.toString() == getString(R.string.button_income)) {
                    readOperationsForWeek(path, OperationType.INCOME)
                } else {
                    readOperationsForWeek(path, OperationType.EXPENSE)
                }

            // Получаем словарь: {категория} - {сумма всех операций по этой категории}
            val result: Map<String, Double> = operations.groupBy({it.categoryName}, {it.amount}).mapValues { it.value.sum() }
            Log.e("TAG", result.toString())

            showData(result)
        }

        // Показ доходов по умолчанию
        toggleBtn.performClick()

        // Кнопка для добавления операции
        binding.addOperationBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addOperationFragment)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showData(data: Map<String, Double>) {
        // Подготовка данных для диаграммы
        val entries = data.map { (label, value) ->
            PieEntry(value.toFloat(), label)
        }
        val totalSum = data.values.sum()

        // Создание набора данных
        val dataSet = PieDataSet(entries, "").apply {
            // Настройка цветов
            colors = listOf(
                Color.rgb(55, 81, 126),   // Темно-синий
                Color.rgb(70, 132, 95),   // Темно-зеленый
                Color.rgb(159, 53, 53),   // Темно-красный
                Color.rgb(124, 85, 46),   // Темно-коричневый
                Color.rgb(95, 47, 104),   // Темно-фиолетовый
                Color.rgb(36, 123, 160),  // Темно-бирюзовый
                Color.rgb(75, 75, 75)     // Темно-серый
            )
            // Отступ между секторами
            sliceSpace = 3f
            // Процент выделения при касании
            selectionShift = 5f

            setDrawValues(true)
            valueTextSize = 16f
            valueTextColor = Color.WHITE
        }

        // Создание объекта данных
        val pieData = PieData(dataSet).apply {
            // Настройка текста значений
            setValueTextSize(12f)
            setValueTextColor(Color.WHITE)
        }

        // Настройка самой диаграммы
        pieChart.apply {
            this.data = pieData

            // Базовые настройки
            setUsePercentValues(true)
            description.isEnabled = false
            // Центральный текст - общая сумма
            centerText = "$totalSum"
            setCenterTextColor(Color.WHITE)
            setCenterTextSize(24f)

            // Анимация при появлении
            animateY(1000)
            // Легенда
            legend.isEnabled = true
            // Поворот диаграммы
            isRotationEnabled = true
        }
    }
}