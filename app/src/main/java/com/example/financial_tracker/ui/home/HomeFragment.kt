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
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter


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
                if (toggleBtn.isChecked) {
                    readOperationsForWeek(path, OperationType.INCOME)
                } else {
                    readOperationsForWeek(path, OperationType.EXPENSE)
                }

            // Получаем словарь: {категория} - {сумма всех операций по этой категории}
            val result: Map<String, Double> = operations.groupBy({it.categoryName}, {it.amount}).mapValues { it.value.sum() }
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

    /**
     * Функция для работы с круговой диаграммой
     */
    private fun showData(data: Map<String, Double>) {
        // Подготовка данных для диаграммы
        val entries = data.map { (label, value) ->
            PieEntry(value.toFloat(), label) // Метка для легенды
        }
        val totalSum = data.values.sum()
        // Округление суммы до двух знаков
        val roundedTotalSum = String.format("%.0f", totalSum)

        // Создание набора данных
        val dataSet = PieDataSet(entries, "").apply {
            // Настройка цветов
            colors = generateColors(entries.size)
            sliceSpace = 3f // Отступ между секторами
            selectionShift = 5f // Процент выделения при касании
            setDrawValues(true) // Отображение значений на диаграмме
            valueTextSize = 12f // Размер текста значений
            valueTextColor = Color.WHITE // Цвет текста значений
        }

        // Создание объекта данных
        val pieData: PieData = PieData(dataSet).apply {
            setValueFormatter(object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    // Форматирование значений: оставить только целые числа
                    return String.format("%.1f", value)
                }
            })
            setValueTextSize(12f)
            setValueTextColor(Color.WHITE)
        }

        // Настройка самой диаграммы
        pieChart.apply {
            this.data = pieData

            // Базовые настройки
            setUsePercentValues(false) // Оставить абсолютные значения
            description.isEnabled = false
            centerText = roundedTotalSum // Центральный текст - округленная сумма
            setCenterTextColor(Color.WHITE)
            setCenterTextSize(20f)
            setDrawEntryLabels(false) // Отключить подписи категорий на диаграмме

            pieChart.setHoleColor(Color.BLACK) // Цвет центральной области
            setTransparentCircleColor(Color.BLACK) // Цвет "бублика"
            setTransparentCircleAlpha(175) // Прозрачность (0-255)

            // Анимация при появлении
            animateY(1000)

            // Легенда
            legend.apply {
                isEnabled = true // Включить легенду
                verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                isWordWrapEnabled = true
                orientation = Legend.LegendOrientation.HORIZONTAL
                textColor = Color.WHITE
                textSize = 16f
            }
            isRotationEnabled = true // Включить поворот диаграммы
        }
    }

    /**
     * Функция для генерации нужного кол-ва цветов для диаграммы
     */
    private fun generateColors(count: Int): List<Int> {
        val colors = mutableListOf<Int>()
        val hueStep = 360 / count // Шаг по цветовому кругу
        for (i in 0 until count) {
            val hue = (i * hueStep) % 360 // Угол на цветовом круге
            colors.add(Color.HSVToColor(floatArrayOf(hue.toFloat(), 0.7f, 0.6f))) // HSV: насыщенность и яркость
        }
        return colors
    }
}
