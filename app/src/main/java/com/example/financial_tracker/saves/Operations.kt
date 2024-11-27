package com.example.financial_tracker.saves
import java.io.File
import java.io.FileWriter
import java.io.BufferedWriter
import java.util.UUID

enum class OperationType {
    INCOME, EXPENSE
}

data class Operation(val id: String, val date: String, val amount: Double,
                     val categoryName: String, val type: OperationType, val comment: String?)

/**
 * Функция для проверки сущесвтования указанного пути и его создания в протвном случае
 */
private fun checkFile(file: File) {
    if (!file.exists()) {
        file.createNewFile()
    }
}

/**
 * Функция для записи операции в файл (csv)
 **/
fun writeOperation(filePath: String, operation: Operation) {
    // TODO - сделать проверку name, чтобы не было строки
    val file = File(filePath)
    checkFile(file)
    file.parentFile?.mkdirs()
    var id: String = UUID.randomUUID().toString() // для генерации неповторимого id
    BufferedWriter(FileWriter(file, true)).use { writer ->
        writer.write("${operation.id},${operation.date},${operation.amount},${operation.categoryName},${operation.type},${operation.comment ?: ""}")
        writer.newLine()
    }
}

/**
 * Функция для чтения всех операций в файле (csv)
 **/
fun readOperations(filePath: String, operationType: OperationType? = null): List<Operation> {
    val file = File(filePath)
    checkFile(file)
    val operations = mutableListOf<Operation>()

    file.bufferedReader().useLines { lines ->
        lines.forEach { line ->
            val fields = line.split(",")
            if (fields.size >= 5) {
                val operation = Operation(
                    id = fields[0],
                    date = fields[1],
                    amount = fields[2].toDouble(),
                    categoryName = fields[3],
                    type = OperationType.valueOf(fields[4]),
                    comment = if (fields.size > 5) fields[5] else null
                )
                if (operationType == null || operation.type == operationType) {
                    operations.add(operation)
                }
            }
        }
    }
    return operations
}

/**
 * Функция для чтения заданной категории в файле (csv)
 **/
fun findOperation(filePath: String, idOperation: String): Operation? {
    val file = File(filePath)
    checkFile(file)

    file.bufferedReader().useLines { lines ->
        lines.forEach { line ->
            val fields = line.split(",")
            if (fields.size >= 5 && fields[0] == idOperation) {
                return Operation(
                    id = fields[0],
                    date = fields[1],
                    amount = fields[2].toDouble(),
                    categoryName = fields[3],
                    type = OperationType.valueOf(fields[4]),
                    comment = if (fields.size > 5) fields[5] else null
                )
            }
        }
    }
    return null
}

/**
 * Функция для обновления операции в файл (csv)
 **/
fun updateOperation(filePath: String, updatedOperation: Operation) {
    val file = File(filePath)
    checkFile(file)
    val operations = readOperations(filePath).map { operation ->
        if (operation.id == updatedOperation.id) updatedOperation else operation
    }
    BufferedWriter(FileWriter(file, false)).use { writer ->
        operations.forEach { operation ->
            writer.write("${operation.id},${operation.date},${operation.amount},${operation.categoryName},${operation.type},${operation.comment ?: ""}")
            writer.newLine()
        }
    }
}

/**
 * Функция для удаления операции в файле (csv)
 **/
fun deleteOperation(filePath: String, idOperation: String) {
    val file = File(filePath)
    checkFile(file)
    val operations = readOperations(filePath).filter {
        it.id != idOperation
    }
    BufferedWriter(FileWriter(file, false)).use { writer ->
        operations.forEach { operation ->
            writer.write("${operation.id},${operation.date},${operation.amount},${operation.categoryName},${operation.type},${operation.comment ?: ""}")
            writer.newLine()
        }
    }
}