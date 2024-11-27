package com.example.financial_tracker.saves

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

data class Category(val name: String, val type: String, val isTemporary: Boolean = false)

/**
 * Функция для проверки сущесвтования указанного пути и его создания в протвном случае
 */
private fun checkFile(file: File) {
    if (!file.exists()) {
        file.createNewFile()
    }
}

/**
 * Функция для записи категории в файл (csv)
**/
fun writeCategory(filePath: String, category: Category) {
    // TODO - сделать проверку name, чтобы не было строки
    val file = File(filePath)
    checkFile(file)
    file.parentFile?.mkdirs() // Создаёт директории, если их нет
    BufferedWriter(FileWriter(file, true)).use { writer -> // true - для записи в конец файла
        writer.write("${category.name},${category.type},,${category.isTemporary}")
        writer.newLine()
    }
}

/**
 * Функция для чтения всех категорий в файле (csv)
 **/
fun readCategories(filePath: String) : List<Category> {
    val file = File(filePath)
    checkFile(file)
    val categories = mutableListOf<Category>()

    file.bufferedReader().useLines { lines ->
        lines.forEach { line ->
            val fields = line.split(",")
            if (fields.size >= 3) {
                val category = Category(
                    name = fields[0],
                    type = fields[1],
                    isTemporary = fields[2].toBoolean()
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
    checkFile(file)

    file.bufferedReader().useLines { lines ->
        lines.forEach { line ->
            val fields = line.split(",")
            if (fields.size >= 3 && fields[0] == categoryName) {
                return Category(
                    name = fields[0],
                    type = fields[1],
                    isTemporary = fields[2].toBoolean()
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
    checkFile(file)
    val categories = readCategories(filePath).map { category ->
        if (category.name == updatedCategory.name) updatedCategory else category
    }
    BufferedWriter(FileWriter(file, false)).use { writer -> // false - для перезаписи файла
        categories.forEach { category ->
            writer.write("${category.name},${category.type},,${category.isTemporary}")
            writer.newLine()
        }
    }
}

/**
 * Функция для удаления категории в файле (csv)
 **/
fun deleteCategoryFromCSV(filePath: String, categoryName: String) {
    val file = File(filePath)
    checkFile(file)
    val categories = readCategories(filePath).filter { it.name != categoryName }
    BufferedWriter(FileWriter(file, false)).use { writer ->
        categories.forEach { category ->
            writer.write("${category.name},${category.type},,${category.isTemporary}")
            writer.newLine()
        }
    }
}