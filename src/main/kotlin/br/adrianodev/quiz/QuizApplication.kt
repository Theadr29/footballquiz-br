package br.adrianodev.quiz

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.runApplication
@SpringBootApplication()
class QuizApplication
//Configure o banco de dados e a porta da aplicação no application.properties
fun main(args: Array<String>) {
	runApplication<QuizApplication>(*args)
}
