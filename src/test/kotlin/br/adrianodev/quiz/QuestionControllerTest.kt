package br.adrianodev.quiz



import br.adrianodev.quiz.model.Question
import br.adrianodev.quiz.repository.QuestionRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
class QuestionControllerTest {

    @Autowired lateinit var mockMvc: MockMvc

    @Autowired lateinit var questionRepository: QuestionRepository


    @Test
    fun `test find all questions`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/questions"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].id").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].perguntas").value("Qual seleção aplicou a maior goleada da história das Copa do Mundo?"))
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].respostas.A").value("Alemanha"))
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].respostaCorreta").value("C"))
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].respostaValor").value("Hungria"))
            .andDo(MockMvcResultHandlers.print())
    }
    @Test
    fun `test find by id`() {
        val question = questionRepository.findAll().first()

        mockMvc.perform(MockMvcRequestBuilders.get("/questions/${question.id}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.id").value(question.id.toInt()))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.perguntas").value(question.perguntas))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.respostas.A").value(question.respostas["A"]))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.respostas.B").value(question.respostas["B"]))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.respostas.C").value(question.respostas["C"]))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.respostas.D").value(question.respostas["D"]))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.respostaCorreta").value(question.respostaCorreta))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.respostaValor").value(question.respostaValor))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `test create question`() {
        val question = Question(
            id = 1,
            perguntas = "Qual seleção aplicou a maior goleada da história das Copa do Mundo?",
            respostas = mapOf("A" to "Alemanha", "B" to "Brasil", "C" to "Hungria", "D" to "Holanda"),
            respostaCorreta = "C",
            respostaValor = "Hungria"
        )
        val json = ObjectMapper().writeValueAsString(question)

        mockMvc.perform(MockMvcRequestBuilders.post("/questions")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.perguntas").value(question.perguntas))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.respostas.A").value(question.respostas["A"]))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.respostas.B").value(question.respostas["B"]))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.respostas.C").value(question.respostas["C"]))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.respostas.D").value(question.respostas["D"]))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.respostaCorreta").value(question.respostaCorreta))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.respostaValor").value(question.respostaValor))
            .andDo(MockMvcResultHandlers.print())

        Assertions.assertFalse(questionRepository.findAll().isEmpty())
    }

    @Test
    fun `test update question`() {
        val question = questionRepository.findAll().first()
        val updatedQuestion = question.copy(perguntas = "Qual seleção venceu a Copa do Mundo de 2018?")

        val json = ObjectMapper().writeValueAsString(updatedQuestion)

        mockMvc.perform(MockMvcRequestBuilders.put("/questions/${question.id}")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.perguntas").value(updatedQuestion.perguntas))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.respostas.A").value(updatedQuestion.respostas["A"]))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.respostas.B").value(updatedQuestion.respostas["B"]))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.respostas.C").value(updatedQuestion.respostas["C"]))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.respostas.D").value(updatedQuestion.respostas["D"]))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.respostaCorreta").value(updatedQuestion.respostaCorreta))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.respostaValor").value(updatedQuestion.respostaValor))
            .andDo(MockMvcResultHandlers.print())

        val findById = questionRepository.findById(question.id!!)
        Assertions.assertTrue(findById.isPresent)
        Assertions.assertEquals(updatedQuestion.perguntas, findById.get().perguntas)
    }

    @Test
    fun `test delete question`() {
        val question = questionRepository.findAll().first()

        mockMvc.perform(MockMvcRequestBuilders.delete("/questions/${question.id}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())

        val findById = questionRepository.findById(question.id!!)
        Assertions.assertFalse(findById.isPresent)
    }

    @Test
    fun `test create question validation error empty perguntas`() {
        val question = Question(
            id = 1,
            perguntas = "", // Campo obrigatório vazio
            respostas = mapOf("A" to "Alemanha", "B" to "Brasil", "C" to "Hungria", "D" to "Holanda"),
            respostaCorreta = "C",
            respostaValor = "Hungria"
        )

        val json = ObjectMapper().writeValueAsString(question)

        mockMvc.perform(MockMvcRequestBuilders.post("/questions")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").value("[perguntas] não pode estar em branco!"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `test create question validation error empty respostas`() {
        val question = Question(
            id = 1,
            perguntas = "Qual seleção aplicou a maior goleada da história das Copa do Mundo?",
            respostas = emptyMap(), // Campo obrigatório vazio
            respostaCorreta = "C",
            respostaValor = "Hungria"
        )

        val json = ObjectMapper().writeValueAsString(question)

        mockMvc.perform(MockMvcRequestBuilders.post("/questions")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").value("[respostas] não pode estar vazio!"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `test create question validation error empty respostaCorreta`() {
        val question = Question(
            id = 1,
            perguntas = "Qual seleção aplicou a maior goleada da história das Copa do Mundo?",
            respostas = mapOf("A" to "Alemanha", "B" to "Brasil", "C" to "Hungria", "D" to "Holanda"),
            respostaCorreta = "", // Campo obrigatório vazio
            respostaValor = "Hungria"
        )

        val json = ObjectMapper().writeValueAsString(question)

        mockMvc.perform(MockMvcRequestBuilders.post("/questions")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").value("[respostaCorreta] não pode estar em branco!"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `test create question validation error empty respostaValor`() {
        val question = Question(
            id = 1,
            perguntas = "Qual seleção aplicou a maior goleada da história das Copa do Mundo?",
            respostas = mapOf("A" to "Alemanha", "B" to "Brasil", "C" to "Hungria", "D" to "Holanda"),
            respostaCorreta = "C",
            respostaValor = "" // Campo obrigatório vazio
        )

        val json = ObjectMapper().writeValueAsString(question)

        mockMvc.perform(MockMvcRequestBuilders.post("/questions")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.statusCode").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").value("[respostaValor] não pode estar em branco!"))
            .andDo(MockMvcResultHandlers.print())
    }
}

