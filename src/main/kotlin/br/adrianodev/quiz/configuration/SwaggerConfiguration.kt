package br.adrianodev.quiz.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfiguration {

    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .apiInfo(
                ApiInfoBuilder()
                .title("FootballQuiz-BR")
                .description("AdrianoDev")
                .version("1.0.0")
                .build())
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(RestController::class.java))
            .build()
            .ignoredParameterTypes(Error::class.java) // Ignorar tipos de parâmetros de erro
            .useDefaultResponseMessages(false) // Desabilitar mensagens de resposta padrão
            .pathMapping("/")

    }

}
