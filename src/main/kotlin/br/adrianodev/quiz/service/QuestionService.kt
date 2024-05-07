package br.adrianodev.quiz.service


import br.adrianodev.quiz.model.Question
import org.springframework.http.ResponseEntity
import java.util.*

interface QuestionService {
    fun create(questions: Question): Question

    fun getAll(): List<Question>

    fun getById(id: Long) : Optional<Question>

    fun updateById(id: Long, question: Question) : Optional<Question>

    fun deleteById(id: Long)
}