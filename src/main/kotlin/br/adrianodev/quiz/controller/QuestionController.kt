package br.adrianodev.quiz.controller

//import br.adrianodev.quiz.configuration.WebSecurityConfiguration
import br.adrianodev.quiz.model.Question
import br.adrianodev.quiz.service.QuestionService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.context.annotation.Import

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.view.RedirectView
import springfox.documentation.annotations.ApiIgnore
import kotlin.random.Random


@RestController
@RequestMapping("/")
class QuestionController(private val service: QuestionService) {

    @PostMapping("/toCreate")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody question: Question): Question = service.create(question)


    @GetMapping("/getQuestions")
    @ApiOperation(value = "Busca todas as perguntas")
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "Exibindo todas as perguntas"),
            ApiResponse(code = 400, message = "Requisição inválida"),
            ApiResponse(code = 404, message = "Pergunta não encontrada"),
            ApiResponse(code = 403, message = "Acesso proibido"),
            ApiResponse(code = 500, message = "Erro interno do servidor")
        ]
    )
    fun getAll(): List<Question> = service.getAll()


    @GetMapping("/getQuestionsById/{id}")
    @ApiOperation(value = "Retorna uma pergunta pelo seu ID")
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "Exibindo uma pergunta pelo id"),
            ApiResponse(code = 400, message = "Requisição inválida"),
            ApiResponse(code = 404, message = "Pergunta não encontrada"),
            ApiResponse(code = 403, message = "Acesso proibido"),
            ApiResponse(code = 500, message = "Erro interno do servidor")
        ]
    )
    fun getQuestion(@PathVariable id: Long): ResponseEntity<Question> =
        service.getById(id).map {
            ResponseEntity.ok(it)
        }.orElse(ResponseEntity.notFound().build())


    @PutMapping("/upById/{id}")
    @ApiOperation(value = "Atualizando uma pergunta pelo seu ID")
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "Atualizando uma pergunta pelo id"),
            ApiResponse(code = 400, message = "Requisição inválida"),
            ApiResponse(code = 404, message = "Pergunta não encontrada"),
            ApiResponse(code = 403, message = "Acesso proibido"),
            ApiResponse(code = 500, message = "Erro interno do servidor")
        ]
    )
    fun updateQuestionById(@PathVariable id: Long, @RequestBody question: Question) : ResponseEntity<Question> =
        service.updateById(id, question).map {
            ResponseEntity.ok(it)
        }.orElse(ResponseEntity.notFound().build())


    @DeleteMapping("/delById/{id}")
    @ApiOperation(value = "Deletando uma pergunta pelo seu ID")
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "Deletando uma pergunta pelo id"),
            ApiResponse(code = 400, message = "Requisição inválida"),
            ApiResponse(code = 404, message = "Pergunta não encontrada"),
            ApiResponse(code = 403, message = "Acesso proibido"),
            ApiResponse(code = 500, message = "Erro interno do servidor")
        ]
    )
    fun deleteQuestionById(@PathVariable id: Long) : ResponseEntity<Void> {
        service.deleteById(id)
        return ResponseEntity<Void>(HttpStatus.OK)
    }
}

