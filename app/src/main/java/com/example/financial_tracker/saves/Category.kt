package com.example.financial_tracker.saves

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

data class Category(val name: String, val color: String, val imagePath: Int, val isTemporary: Boolean = false)

/**
 * Функция для записи категории в файл (csv)
**/
fun writeCategory(filePath: String, category: Category) {
    // TODO - сделать проверку name, чтобы не было строки
    val file = File(filePath)
    file.parentFile?.mkdirs() // Создаёт директории, если их нет
    BufferedWriter(FileWriter(file, true)).use { writer -> // true - для записи в конец файла
        writer.write("${category.name},${category.color},${category.imagePath},${category.isTemporary}")
        writer.newLine()
    }
}

/**
 * Функция для чтения всех категорий в файле (csv)
 **/
fun readCategories(filePath: String) : List<Category> {
    val file = File(filePath)
    val categories = mutableListOf<Category>()

    file.bufferedReader().useLines { lines ->
        lines.forEach { line ->
            val fields = line.split(",")
            if (fields.size >= 4) {
                val category = Category(
                    name = fields[0],
                    color = fields[1],
                    imagePath = fields[2].toInt(),
                    isTemporary = fields[3].toBoolean()
                )
                categories.add(category)
            }
        }
    }
    return categories
}

/**
 * Функция для чтения заданной категории в файле (csv)
 **/
fun findCategory(filePath: String, categoryName: String): Category? {
    val file = File(filePath)

    file.bufferedReader().useLines { lines ->
        lines.forEach { line ->
            val fields = line.split(",")
            if (fields.size >= 4 && fields[0] == categoryName) {
                return Category(
                    name = fields[0],
                    color = fields[1],
                    imagePath = fields[2].toInt(),
                    isTemporary = fields[3].toBoolean()
                )
            }
        }
    }
    return null // Категория не найдена
}

/**
 * Функция для обновления категории в файл (csv)
 **/
fun updateCategory(filePath: String, updatedCategory: Category) {
    val file = File(filePath)
    val categories = readCategories(filePath).map { category ->
        if (category.name == updatedCategory.name) updatedCategory else category
    }
    BufferedWriter(FileWriter(file, false)).use { writer -> // false - для перезаписи файла
        categories.forEach { category ->
            writer.write("${category.name},${category.color},${category.imagePath},${category.isTemporary}")
            writer.newLine()
        }
    }
}

/**
 * Функция для удаления категории в файле (csv)
 **/
fun deleteCategoryFromCSV(filePath: String, categoryName: String) {
    val file = File(filePath)
    val categories = readCategories(filePath).filter { it.name != categoryName }
    BufferedWriter(FileWriter(file, false)).use { writer ->
        categories.forEach { category ->
            writer.write("${category.name},${category.color},${category.imagePath},${category.isTemporary}")
            writer.newLine()
        }
    }
}