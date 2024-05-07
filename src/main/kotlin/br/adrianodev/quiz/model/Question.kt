package br.adrianodev.quiz.model


import javax.persistence.*
@Entity(name = "questions")
data class Question(
    @Id @GeneratedValue
    val id: Long,
    var perguntas: String,
    @ElementCollection
    @CollectionTable(name = "respostas", joinColumns = [JoinColumn(name = "question_id")])
    @MapKeyColumn(name = "resposta_key")
    @Column(name = "resposta_value")
    var respostas: Map<String, String>,
    var respostaCorreta: String,
    var respostaValor: String,
)
