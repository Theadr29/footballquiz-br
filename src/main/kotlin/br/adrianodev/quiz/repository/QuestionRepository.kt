package br.adrianodev.quiz.repository

import br.adrianodev.quiz.model.Question
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface QuestionRepository : JpaRepository<Question, Long> {
     override fun findById(id: Long): Optional<Question>
}
