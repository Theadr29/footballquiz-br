package br.adrianodev.quiz.service

import br.adrianodev.quiz.model.Question
import br.adrianodev.quiz.repository.QuestionRepository
import br.adrianodev.quiz.service.QuestionService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class QuestionServiceImpl(private val repository: QuestionRepository) : QuestionService {

    override fun create(questions: Question): Question {

        return repository.save(questions)
    }

    override fun getAll(): List<Question> {

        return repository.findAll()
    }

    override fun getById(id: Long): Optional<Question> {

        return repository.findById(id)
    }

    override fun updateById(id: Long, question: Question): Optional<Question> {
        val optional = getById(id)
        if (!optional.isPresent) {
            return optional
        }

        val existingQuestion = optional.get()
        existingQuestion.apply {
            perguntas = question.perguntas
            respostas = question.respostas
            respostaCorreta = question.respostaCorreta
            respostaValor = question.respostaValor
        }
        repository.save(existingQuestion)

        return Optional.of(existingQuestion)
    }

    override fun deleteById(id: Long) {
        repository.findById(id).map {
            repository.delete(it)
        }.orElseThrow { RuntimeException("Id not found $id") }
    }


}
