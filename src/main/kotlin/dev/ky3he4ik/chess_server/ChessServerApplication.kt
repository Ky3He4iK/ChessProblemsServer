package dev.ky3he4ik.chess_server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2


@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
@EnableSwagger2
class ChessServerApplication {
    @Bean
    fun productApi(): Docket? {
        return Docket(DocumentationType.SWAGGER_2).select()
            .apis(RequestHandlerSelectors.basePackage("dev.ky3he4ik.chess_server")).build()
    }
}

fun main(args: Array<String>) {
    runApplication<ChessServerApplication>(*args)
}
